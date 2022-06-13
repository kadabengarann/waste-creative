package com.wastecreative.wastecreative.utils

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun String.getTimeAgo(): String? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(this) as Date
        if (date.time < 1000000000000L) {
            date.time *= 1000
        }

        val hah = Calendar.getInstance()
        Log.d("TAGAGAGAGA", "getTimeAgo: $hah")
        Log.d("TAGAGAGAGA", "getTimeAgoDATA: $date")
        val now = System.currentTimeMillis()
        if (date.time > now || date.time <= 0) {
            return null
        }
        val diff = now - date.time
        return if (diff < MINUTE_MILLIS) {
            "just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "a minute ago"
        } else if (diff < 50 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " minutes ago"
        } else if (diff < 90 * MINUTE_MILLIS) {
            "an hour ago"
        } else if (diff < 24 * HOUR_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hours ago"
        } else if (diff < 48 * HOUR_MILLIS) {
            "yesterday"
        } else if (diff < 72 * HOUR_MILLIS) {
            (diff / DAY_MILLIS).toString() + " days ago"
        }else{
            return DateFormat.getDateInstance(DateFormat.FULL).format(date)
        }
    }
