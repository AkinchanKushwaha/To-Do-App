package com.example.todo.fragments


import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*


class SharedViewModelTest {

    private lateinit var mSharedViewModel: SharedViewModel

    @Before
    fun setUp() {
        mSharedViewModel = SharedViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun titleAndTheDescriptionNotEmpty_returnsTrue() {
        val result = mSharedViewModel
            .verifyData(
                "testTitle",
                "testDescription",
            )
        assertThat(result).isTrue()
    }

    @Test
    fun titleAndTheDescriptionEmpty_returnsFalse() {
        val result = mSharedViewModel
            .verifyData(
                "",
                "",
            )
        assertThat(result).isFalse()
    }

    @Test
    fun dueDateAndTimeIsEqualToCurrentTime_returnsFalse() {
        val currentTime = Calendar.getInstance().timeInMillis
        val result = mSharedViewModel.verifyDateAndTime(currentTime)
        assertThat(result).isFalse()
    }

    @Test
    fun dueDateAndTimeIsEqualToZero_returnsFalse() {
        val result = mSharedViewModel.verifyDateAndTime(0.toLong())
        assertThat(result).isFalse()
    }

    @Test
    fun dueDateAndTimeIsCorrect_returnsTrue() {
        // Adding 10 hours in current time
        val dueTime = Calendar.getInstance().timeInMillis + 36000000
        val result = mSharedViewModel.verifyDateAndTime(dueTime)
        assertThat(result).isTrue()
    }

}