package com.example.weatherapp.ui.home.weatherHistory

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherHistoryFragment : Fragment() {

    private var binding: FragmentWeatherHistoryBinding? = null
    private val weatherHistoryViewModel: WeatherHistoryViewModel by viewModels()
    private val historyListAdapter by lazy { WeatherHistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupMenu()
        setObservers()
    }

    private fun init() {
        binding?.apply {
            rvWeatherHistory.adapter = historyListAdapter
            rvWeatherHistory.addItemDecoration(
                DividerItemDecoration(
                    rvWeatherHistory.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.clear_action_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                val deleteMenu = menu.findItem(R.id.menu_clear_history)
                deleteMenu.isVisible = historyListAdapter.differ.currentList.isNotEmpty()
                super.onPrepareMenu(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_clear_history -> {
                        handleClearHistory()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setObservers() {

        weatherHistoryViewModel.weatherHistory.observe(viewLifecycleOwner) { history ->
            if (history.isEmpty()) {
                binding?.llNoHistory?.visibility = View.VISIBLE
                binding?.rvWeatherHistory?.visibility = View.GONE
            } else {
                binding?.llNoHistory?.visibility = View.GONE
                binding?.rvWeatherHistory?.visibility = View.VISIBLE
                historyListAdapter.differ.submitList(history)
            }
        }

        weatherHistoryViewModel.isHistoryCleared.observe(this) { isCleared ->
            if (!weatherHistoryViewModel.isClearHistoryToastShown) {
                if (!isCleared) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_failed_delete_history),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.success_history_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                weatherHistoryViewModel.setClearHistoryToastShown(true)
            }
        }
    }

    private fun handleClearHistory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle(getString(R.string.confirm_clear_history))
            setMessage(getString(R.string.erase_history_forever))
            setPositiveButton(getString(R.string.yes)) { dialog: DialogInterface, _ ->
                weatherHistoryViewModel.clearWeatherHistory()
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.no)) { dialog: DialogInterface, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}