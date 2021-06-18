package com.example.todo.fragments


import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test


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

}