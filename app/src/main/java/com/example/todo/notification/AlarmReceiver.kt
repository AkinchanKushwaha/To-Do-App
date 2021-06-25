package com.example.todo.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        service.putExtra("notificationTitle", intent.getStringExtra("notificationTitle"))
        service.putExtra(
            "notificationDescription",
            intent.getStringExtra("notificationDescription")
        )

        NotificationService.enqueueWork(context, service)
    }

}