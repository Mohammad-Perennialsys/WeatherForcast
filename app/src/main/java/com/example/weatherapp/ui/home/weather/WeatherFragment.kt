package com.example.weatherapp.ui.home.weather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.common.utils.Constants.PERMISSION_FINE_LOCATION
import com.example.weatherapp.common.utils.NetworkHelper
import com.example.weatherapp.common.utils.convertToLocalTime
import com.example.weatherapp.common.utils.loadFromUrl
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.databinding.FragmentWeatherBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WeatherFragment"

enum class ViewState { LOADING, ERROR, SUCCESS }

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var _binding: FragmentWeatherBinding? = null
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fetchCurrentWeather()
                Toast.makeText(requireContext(), "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                if (hasLocationRationalShown()) {
                    showAppSettingsDialog()
                }
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }

    private val resolutionForResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Device Location is Enabled! ", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Location is Not Enabled.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            fetchCurrentWeather()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    weatherViewModel.setLocation(
                        lat = location.latitude, lon = location.longitude
                    )
                    fetchCurrentWeather()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentWeather()
        setObservers()
    }

    private fun fetchCurrentWeather() {
        if (hasLocationPermission()) {
            if (weatherViewModel.currentWeather == null) {
                if (!hasCurrentLocation()) {
                    getLastLocation()
                } else {
                    if (isInternetAvailable()) {
                        weatherViewModel.fetchCurrentWeather()
                    } else {
                        showNoInternetDialog()
                    }
                }
            }
        } else {
            checkLocationPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }

    private fun isInternetAvailable(): Boolean {
        return NetworkHelper.isNetworkAvailable(requireContext())
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), PERMISSION_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasCurrentLocation(): Boolean = weatherViewModel.currentLocation != null

    private fun hasLocationRationalShown(): Boolean {
        return !shouldShowRequestPermissionRationale(PERMISSION_FINE_LOCATION)
    }

    private fun setObservers() {
        weatherViewModel.weatherUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    setViewsVisibility(ViewState.LOADING)
                }
                is UiState.Success -> {
                    setViewsVisibility(ViewState.SUCCESS)
                    updateViews(data = state.data)
                }
                is UiState.Error -> {
                    setViewsVisibility(ViewState.ERROR)
                    Toast.makeText(
                        requireContext(), "Error : ${state.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateViews(data: WeatherInfo?) {
        data?.let {
            val zonId = it.timeZoneId ?: 0
            _binding?.apply {
                tvLocation.text = getString(R.string.location_city_state, it.city, it.country)
                tvCondition.text = it.description
                tvTemprature.text = getString(
                    R.string.temp_in_celsius, it.temperature
                )
                tvSunriseTime.text = it.sunrise.convertToLocalTime(zonId)
                tvSunsetTime.text = it.sunset.convertToLocalTime(zonId)
                ivWeatherLogo.loadFromUrl(getString(R.string.weather_icon_url, it.weatherIcon))
            }
        }
    }

    private fun setViewsVisibility(status: ViewState) {
        _binding?.apply {
            when (status) {
                ViewState.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    weatherViewContainer.visibility = View.INVISIBLE
                }
                ViewState.ERROR -> {
                    progressBar.visibility = View.INVISIBLE
                    weatherViewContainer.visibility = View.INVISIBLE
                }
                ViewState.SUCCESS -> {
                    progressBar.visibility = View.INVISIBLE
                    weatherViewContainer.visibility = View.VISIBLE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (hasLocationPermission()) {
            if (!hasCurrentLocation()) {
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location == null) {
                            requestLocationUpdate()
                        } else {
                            weatherViewModel.setLocation(
                                lat = location.latitude, lon = location.longitude
                            )
                            fetchCurrentWeather()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Location Exception :${it.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } else {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), PERMISSION_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchCurrentWeather()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), PERMISSION_FINE_LOCATION
            ) -> {
                showRationalDialog()
            }

            else -> {
                requestLocationPermissionLauncher.launch(PERMISSION_FINE_LOCATION)
            }
        }
    }

    private fun requestLocationUpdate() {
        startLocationUpdate()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMaxUpdates(1)
                .build()

        // check settings and enable device location
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())
        val task = client.checkLocationSettings(locationSettingsRequest.build())

        task.addOnSuccessListener { locationSettingsResponse ->
            Log.d(TAG, "startLocationUpdate: $locationSettingsResponse")
            fusedLocationProviderClient
                .requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper()
                )
        }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.resolution).build()
                        resolutionForResultLauncher.launch(intentSenderRequest)

                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.d(TAG, "startLocationUpdate: ${sendEx.localizedMessage}")
                        return@addOnFailureListener
                    }
                } else {
                    val apiException = (exception as? ApiException)?.status
                    apiException?.let { status ->
                        when (status.statusCode) {
                            8502 -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Location Settings is Unavailable",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Location Settings Error: ${status.statusMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    Log.d(TAG, "startLocationUpdate: ${exception.localizedMessage}")
                }
            }
    }

    private fun showRationalDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Permission is Required!")
            setMessage("This App requires Location Permission to fetch Current Weather")
            setCancelable(false)
            setPositiveButton("allow") { dialog: DialogInterface, _ ->
                requestLocationPermissionLauncher.launch(PERMISSION_FINE_LOCATION)
                dialog.cancel()
            }
            setNegativeButton("cancel") { dialog: DialogInterface, _ ->
                dialog.cancel()
            }
        }.show()
    }

    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("No Internet Connection!")
            setMessage("Please Check your internet connection & try again")
            setCancelable(false)
            setPositiveButton("Retry") { dialog: DialogInterface, _ ->
                fetchCurrentWeather()
                dialog.cancel()
            }
        }.show()
    }

    /***
     * Dialog to open App Settings to Allow Permission via App Settings
     * if User completely Denied Location Permission and Can't proceed with out Location Permission
     */
    private fun showAppSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Permission is Required")
            setMessage("Permission is needed to feature weather,please allow the location permission")
            setCancelable(false)
            setPositiveButton("Settings") { dialog: DialogInterface, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.parse("package:${context.packageName}")
                intent.data = uri
                startActivity(intent)
                dialog.cancel()
            }
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}