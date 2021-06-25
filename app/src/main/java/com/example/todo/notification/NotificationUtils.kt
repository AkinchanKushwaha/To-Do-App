package com.example.todo.notification

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*


class NotificationUtils {

    fun setNotification(
        reason: String,
        notificationID: Int,
        notificationTitle: String,
        notificationDescription: String,
        timeInMilliSeconds: Long,
        activity: Activity
    ) {

        //------------  alarm settings start  -----------------//

        if (timeInMilliSeconds > 0) {


            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(
                activity.applicationContext,
                AlarmReceiver::class.java
            ) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra("reason", reason)
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
            alarmIntent.putExtra("notificationTitle", notificationTitle)
            alarmIntent.putExtra("notificationDescription", notificationDescription)
            alarmIntent.putExtra("notificationID", notificationID)


            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds


            val pendingIntent = PendingIntent.getBroadcast(
                activity,
                0,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        }
        //------------ end of alarm settings  -----------------//

    }

    fun clearNotification(reason: String, notificationID: Int, activity: Activity) {

        val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(
            activity.applicationContext,
            AlarmReceiver::class.java
        ) // AlarmReceiver1 = broadcast receiver

        alarmIntent.putExtra("reason", reason)
        alarmIntent.putExtra("notificationID", notificationID)


        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis,
            pendingIntent
        )

    }

    fun clearAllNotifications(reason: String, activity: Activity) {
        val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(
            activity.applicationContext,
            AlarmReceiver::class.java
        )
        alarmIntent.putExtra("reason", reason)

        val pendingIntent = PendingIntent.getBroadcast(
            activity,
            0,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis,
            pendingIntent
        )
    }
}