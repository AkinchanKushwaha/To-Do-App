package com.example.todo.fragments

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todo.R
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class BindingAdapters {
    companion object {

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
                }
                Priority.MEDIUM -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
                }
                Priority.LOW -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
                }
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData) {
            view.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }

        }

        @BindingAdapter("android:showSwipeToLeftHint")
        @JvmStatic
        fun showSwipeToLeftHint(view: TextView, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.INVISIBLE
                false -> view.visibility = View.VISIBLE
            }
        }

        @BindingAdapter("android:longToText")
        @JvmStatic
        fun longToText(view: TextView, currentItem: ToDoData) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentItem.dueTime

            val mYear = calendar[Calendar.YEAR]
            val mMonth = calendar[Calendar.MONTH]
            val mDay = calendar[Calendar.DAY_OF_MONTH]
            val mTimeHour = calendar[Calendar.HOUR]
            val mTimeMinute = calendar[Calendar.MINUTE]
            view.text = "$mDay/$mMonth/$mYear, $mTimeHour:$mTimeMinute"
        }
    }
}