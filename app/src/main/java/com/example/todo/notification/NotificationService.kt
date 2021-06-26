package com.example.todo.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log.d
import androidx.core.app.JobIntentService
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.utils.Constants


class NotificationService : JobIntentService() {
    private lateinit var mNotification: Notification
    private lateinit var mReason: String
    private lateinit var mNotificationTitle: String
    private lateinit var mNotificationDescription: String

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

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NotificationService::class.java, 1, intent)
        }

        const val CHANNEL_ID = "dueDateAndTimeNotificationID"
        const val CHANNEL_NAME = "dueDateAndTimeNotification"

    }

    override fun onDestroy() {
        d("notification service", "Service stopped")
        super.onDestroy()
    }

    override fun onHandleWork(intent: Intent) {
        val notificationID: Int
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Create Channel
        createChannel()

        if (intent.extras != null) {
            mReason = intent.extras!!.getString("reason")!!
        }


        when (mReason) {
            /**
             * When the reason is
             *      NOTIFICATION_ADD then add a new notification
             */
            Constants.NOTIFICATION_ADD -> {


//                val timestamp = intent.extras!!.getLong("timestamp")
                mNotificationTitle = intent.extras!!.getString("notificationTitle")!!
                mNotificationDescription = intent.extras!!.getString("notificationDescription")!!
                notificationID = intent.extras!!.getInt("notificationID")


                val context = this.applicationContext
                val notifyIntent = Intent(this, MainActivity::class.java)


                notifyIntent.putExtra("title", mNotificationTitle)
                notifyIntent.putExtra("message", mNotificationDescription)
                notifyIntent.putExtra("notification", true)

                notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK


                val pendingIntent = PendingIntent.getActivity(
                    context,
                    notificationID,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val res = this.resources

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                    mNotification = Notification.Builder(this, CHANNEL_ID)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic__notification)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setContentTitle(mNotificationTitle)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(mNotificationDescription)
                        )
                        .setContentText(mNotificationDescription).build()
                } else {

                    mNotification = Notification.Builder(this, "ChannelID")
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic__notification)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                        .setAutoCancel(true)
                        .setContentTitle(mNotificationTitle)
                        .setStyle(
                            Notification.BigTextStyle()
                                .bigText(mNotificationDescription)
                        )
                        .setContentText(mNotificationDescription).build()
                }

                notificationManager.notify(notificationID, mNotification)


            }
            /**
             * When the reason is NOTIFICATION_CANCEL
             *      then CANCEL the existing notification with the ID of notificationID
             */
            Constants.NOTIFICATION_CANCEL -> {
                notificationID = intent.extras!!.getInt("notificationID")
                notificationManager.cancel(notificationID)
            }

            /**
             * When the reason is NOTIFICATION_CLEAR_ALL
             *      then CLEAR all the existing notifications
             */
            Constants.NOTIFICATION_CANCEL_ALL -> {
                notificationManager.cancelAll()
            }

        }

    }
}