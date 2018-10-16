package com.roombookingapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Patterns
import com.roombookingapp.data.model.AvailableSlots
import java.text.SimpleDateFormat
import java.util.*

/**
 * This class to implement the common functionlities for the app
 */
class CommonUtil {


    companion object {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy")

        /*
       Convert dd-MM-yyyy HH:mm to date
        */
        fun convertIntoDateFormat(dateText: String): Date {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val date = dateFormat.parse(dateText)
            return date
        }

        /*
         Convert current date to dd-MM-yyyy
          */
        fun getTheCurrentDate(): String {
            val currentDate = Date()
            return dateFormatter.format(currentDate.time)

        }

        /*
        Add or minus the date from existing date
         */
        fun setTheDate(no: Int, date: String): String? {

            val cal = Calendar.getInstance()
            cal.time = dateFormatter.parse(date)
            cal.add(Calendar.DATE, no)
            return dateFormatter.format(cal.getTime());
        }

        /*
        Convert dd-MM-yyyy to unixtime(milliseconds)
         */
        fun getTheDateinUnixTime(datetext: String): String? {
            val date = dateFormatter.parse(datetext)
            return date.time.toString();
        }

        /*
        Add the Every time 15 minutes to current time
         */


        fun addMinutesToDate(times: Int, beforeTime: Date): Date {
            val FIFTEEN_MINUTE_IN_MILLIS: Long = 15 * 60000//millisecs
            val curTimeInMs = beforeTime.time
            return Date(curTimeInMs + (times * FIFTEEN_MINUTE_IN_MILLIS).toInt())
        }

        fun convertAvailableTextToDate(value: String, dateText: String): AvailableSlots {
            val st = value.split("-")
            val startDate = CommonUtil.convertIntoDateFormat(dateText + " " + st[0])
            val endDate = CommonUtil.convertIntoDateFormat(dateText + " " + st[1])
            return AvailableSlots(startDate!!, endDate!!)
        }


        fun getHoursMinutes(date: Date): String {
            return String.format(
                    "%02d:%02d", date.hours, date.minutes)
        }

        fun isNetworkStatusAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            connectivityManager?.let {
                it.activeNetworkInfo?.let {
                    if (it.isConnected) return true
                }
            }
            return false
        }


        fun isValidEmail(target: String): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }


        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return !TextUtils.isEmpty(phoneNumber) && phoneNumber.length > 10 && phoneNumber.length < 13 && android.util.Patterns.PHONE.matcher(phoneNumber).matches()
        }
    }


}