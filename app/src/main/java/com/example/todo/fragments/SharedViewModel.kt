package com.example.todo.fragments

import android.app.Activity
import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.notification.NotificationUtils
import com.example.todo.utils.Constants
import java.util.*


class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseIsEmpty(todoData: List<ToDoData>) {
        emptyDatabase.value = todoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as? TextView)?.setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.red
                    )
                )
                1 -> (parent?.getChildAt(0) as? TextView)?.setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.yellow
                    )
                )
                2 -> (parent?.getChildAt(0) as? TextView)?.setTextColor(
                    ContextCompat.getColor(
                        application,
                        R.color.green
                    )
                )
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }

    fun verifyData(title: String, description: String): Boolean {
        return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            false
        } else !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    fun verifyDateAndTime(mDueTimeAndDate: Long): Boolean {
        val currentTime = Calendar.getInstance().timeInMillis / 1000
        val roundedOffDateAndTime = mDueTimeAndDate / 1000

        return !(mDueTimeAndDate == 0.toLong() || roundedOffDateAndTime == currentTime || roundedOffDateAndTime < currentTime)
    }

    fun timeInMillisToString(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis

        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        val mTimeHour = calendar[Calendar.HOUR]
        val mTimeMinute = calendar[Calendar.MINUTE]
        val mAMorPM = if (calendar[Calendar.AM_PM] == Calendar.AM) "AM" else "PM"

        return "$mDay/$mMonth/$mYear, $mTimeHour:$mTimeMinute $mAMorPM"
    }

    fun scheduleNotification(
        notificationID: Int,
        notificationTitle: String,
        notificationDescription: String,
        notificationDueDatAndTime: Long,
        activity: Activity,
    ) {
        val mNotificationTitle = "Hello! '$notificationTitle' is due now."
        NotificationUtils().setNotification(
            Constants.NOTIFICATION_ADD,
            notificationID,
            mNotificationTitle,
            notificationDescription,
            notificationDueDatAndTime,
            activity
        )
    }

    fun clearNotification(notificationID: Int, activity: Activity) {
        NotificationUtils().clearNotification(
            Constants.NOTIFICATION_CANCEL,
            notificationID,
            activity
        )
    }

    fun clearAllNotification(activity: Activity) {
        NotificationUtils().clearAllNotifications(Constants.NOTIFICATION_CANCEL_ALL, activity)
    }


}