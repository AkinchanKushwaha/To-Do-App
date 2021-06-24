package com.example.todo.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import com.example.todo.MainActivity
import com.example.todo.R
import java.util.*

// TODO: Intent Service has been deprecated. Fix it.
class NotificationService : IntentService("NotificationService") {
    private lateinit var mNotification: Notification

    @SuppressLint("NewApi")
    private fun createChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val context = this.applicationContext
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = getString(R.string.notification_channel_description)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        const val CHANNEL_ID = "dueDateAndTimeNotificationID"
        const val CHANNEL_NAME = "dueDateAndTimeNotification"

    }


    override fun onHandleIntent(intent: Intent?) {

        //Create Channel
        createChannel()


        var timestamp: Long = 0
        var notificationTitle = "notificationTitle"
        var notificationDescription = "notificationDescription"
        var notificationID = 0

        if (intent != null && intent.extras != null) {

            timestamp = intent.extras!!.getLong("timestamp")
            notificationTitle = intent.extras!!.getString("notificationTitle")!!
            notificationDescription = intent.extras!!.getString("notificationDescription")!!
            notificationID = intent.extras!!.getInt("notificationID")
        }




        if (timestamp > 0) {


            val context = this.applicationContext
            val notifyIntent = Intent(this, MainActivity::class.java)


            notifyIntent.putExtra("title", notificationTitle)
            notifyIntent.putExtra("message", notificationDescription)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp


            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val res = this.resources

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(notificationTitle)
                    .setStyle(
                        Notification.BigTextStyle()
                            .bigText(notificationDescription)
                    )
                    .setContentText(notificationDescription).build()
            } else {

                mNotification = Notification.Builder(this, "ChannelID")
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setContentTitle(notificationTitle)
                    .setStyle(
                        Notification.BigTextStyle()
                            .bigText(notificationDescription)
                    )
                    .setContentText(notificationDescription).build()

            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define

            notificationManager.notify(notificationID, mNotification)
        }


    }
}