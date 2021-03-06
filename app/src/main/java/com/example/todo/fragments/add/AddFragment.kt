package com.example.todo.fragments.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log.d
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.TodoViewModel
import com.example.todo.databinding.FragmentAddBinding
import com.example.todo.fragments.SharedViewModel
import java.util.*

class AddFragment : Fragment() {


    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var mDueTimeAndDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)


        // Set Menu
        setHasOptionsMenu(true)

        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        binding.dueDateAndTimeLl.setOnClickListener {
            pickDateTime()
        }


        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()
        val mNotificationID: Int = getNotificationId()

        val validation = mSharedViewModel.verifyData(mTitle, mDescription)
        val validationOfDateAndTime = mSharedViewModel.verifyDateAndTime(mDueTimeAndDate)

        if (validation && validationOfDateAndTime) {
            // Insert Data to DB
            val newData =
                ToDoData(
                    0,
                    mTitle,
                    mSharedViewModel.parsePriority(mPriority),
                    mDescription,
                    mDueTimeAndDate,
                    mNotificationID
                )
            mTodoViewModel.insertData(newData)
            d("Fragment Add", "${Calendar.getInstance().timeInMillis}  $mDueTimeAndDate")
            mSharedViewModel.scheduleNotification(
                mNotificationID,
                mTitle,
                mDescription,
                mDueTimeAndDate,
                requireActivity()
            )


            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            if (!validation) {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_SHORT)
                    .show()
            }
            if (!validationOfDateAndTime) {
                Toast.makeText(
                    requireContext(),
                    "Please choose a valid Date and Time!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    val pickedDateTime = Calendar.getInstance()
                    pickedDateTime.set(year, month, day, hour, minute)
                    mDueTimeAndDate = pickedDateTime.timeInMillis
                    setupDateAndTime(pickedDateTime)
                },
                startHour,
                startMinute,
                false
            ).show()
        }, startYear, startMonth, startDay).show()

    }

    private fun setupDateAndTime(calendar: Calendar) {
        binding.dueDateAndTimeTv.text = mSharedViewModel.timeInMillisToString(calendar.timeInMillis)
    }


    private fun getNotificationId(): Int {
        // TODO: Look for other ways to generate ID
        return (Calendar.getInstance().timeInMillis / 1000).toInt()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}