package com.example.todo.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.data.models.ToDoData
import com.example.todo.data.viewModel.TodoViewModel
import com.example.todo.databinding.FragmentUpdateBinding
import com.example.todo.fragments.SharedViewModel
import java.util.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private var mCurrentDueDateAndTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        // Set Menu
        setHasOptionsMenu(true)


        binding.currentTitleEt.setText(args.currentItem.title)
        binding.currentDescriptionEt.setText(args.currentItem.description)
        binding.currentPrioritiesSpinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.currentPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        binding.currentDueDateAndTimeTv.text =
            mSharedViewModel.timeInMillisToString(args.currentItem.dueTime)
        binding.currentDateAndTimePickerIv.setOnClickListener {
            updateDateTime(args.currentItem.dueTime)
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val getPriority = binding.currentPrioritiesSpinner.selectedItem.toString()
        val notificationId = args.currentItem.notificationID

        // Verify the updated data
        val validation = mSharedViewModel.verifyData(title, description)
        val dateAndTimeValidation = mSharedViewModel.verifyDateAndTime(mCurrentDueDateAndTime)

        // If data is valid then update it and schedule a notification.
        if (validation && dateAndTimeValidation) {
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description,
                // TODO make a validation function for updated date and time.
                mCurrentDueDateAndTime,
                notificationId
            )
            mTodoViewModel.updateData(updatedItem)
            mSharedViewModel.scheduleNotification(
                notificationId,
                title,
                description,
                requireActivity()
            )

            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()

            // Navigate back to list fragment after data is updated
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)

        } else {
            if (!validation) {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                    .show()
            }
            if (!dateAndTimeValidation) {
                Toast.makeText(
                    requireContext(),
                    "Please choose a valid Date and Time.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateDateTime(currentDueTime: Long) {
        val currentDateTime = Calendar.getInstance()
        currentDateTime.timeInMillis = currentDueTime

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
                    mCurrentDueDateAndTime = pickedDateTime.timeInMillis
                    binding.currentDueDateAndTimeTv.text =
                        mSharedViewModel.timeInMillisToString(pickedDateTime.timeInMillis)
                },
                startHour,
                startMinute,
                false
            ).show()
        }, startYear, startMonth, startDay).show()

    }

    // Show AlertDialog to confirm item removal
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed: '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}' ?")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}' ?")
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}