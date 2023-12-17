package com.example.weatherapp.common.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.example.weatherapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * Function to Clear Error Message on text change in Input EditTextField
 * @param inputLayout material component inputLayout
 */
fun TextInputEditText.clearErrorOnTextChanged(inputLayout: TextInputLayout) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // implementation not Needed
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // implementation not Needed
        }
        override fun afterTextChanged(s: Editable?) { inputLayout.error = null }
    })
}

/**
 * convertToLocalTime is Utility Helper Function to Convert unix timestamp to Time
 * @param zoneId shift in seconds from UTC
 * @return Time in String with format ex: 12:30 PM
 */
fun Int.convertToLocalTime(zoneId: Int): String {
    val calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calender.timeInMillis = this * 1000L // convert seconds to milliseconds
    calender.add(Calendar.SECOND, zoneId)
    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    simpleDateFormat.timeZone = calender.timeZone
    return simpleDateFormat.format(calender.time)
}

fun ImageView.loadFromUrl(url: String) {
    Picasso.get()
        .load(url)
        .placeholder(R.mipmap.ic_launcher_foreground)
        .error(R.mipmap.ic_launcher_foreground)
        .into(this)
}

/***
 *
 * Function to convert TimeStamp to Readable LocalDateTime format
 * @return String - 01-Jan-2023 12:00 AM/PM
 *
 */
fun Long.timeStampToLocalDateTime(): String {
    val sdf = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH)
    return sdf.format(Date(this))
}