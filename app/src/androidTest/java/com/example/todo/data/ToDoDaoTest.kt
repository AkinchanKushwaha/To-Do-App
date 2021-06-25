package com.example.todo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.data.models.Priority
import com.example.todo.data.models.ToDoData
import com.example.todo.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ToDoDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TodoDatabase
    private lateinit var dao: ToDoDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.todoDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    /**
     * insertTodoItem test passes when the list contains the todoItem that was added in the list.
     */
    @Test
    fun insertTodoItem() = runBlockingTest {
        val todoItem = ToDoData(1, "title", Priority.LOW, "description", 12341234L, 1234)
        dao.insertData(todoItem)

        val todoList = dao.getAllData().getOrAwaitValue()

        assertThat(todoList).contains(todoItem)
    }

    /**
     * deleteTodoItem test passes when the list doesn't contain the todoItem that was deleted from
     * the list.
     */
    @Test
    fun deleteTodoItem() = runBlockingTest {
        val todoItem = ToDoData(1, "title", Priority.LOW, "description", 12341234L, 1234)
        dao.insertData(todoItem)
        dao.deleteItem(todoItem)

        val todoList = dao.getAllData().getOrAwaitValue()

        assertThat(todoList).doesNotContain(todoItem)
    }

    /**
     * deleteAllItems test passes when todoList is empty after adding multiple todoItems in list.
     */
    @Test
    fun deleteAllItems() = runBlockingTest {
        val todoItem1 = ToDoData(1, "title", Priority.LOW, "description", 12341234L, 1234)
        val todoItem2 = ToDoData(2, "title", Priority.LOW, "description", 12341234L, 11234)

        dao.insertData(todoItem1)
        dao.insertData(todoItem2)
        dao.deleteAll()

        val todoList = dao.getAllData().getOrAwaitValue()

        assertThat(todoList).isEmpty()
    }

    /**
     * updateItem passes when todoList contains the todoItemUpdated and it doesn't contain the
     * todoItem i.e. item before it was updated.
     */
    @Test
    fun updateItem() = runBlockingTest {
        val todoItem = ToDoData(1, "title", Priority.LOW, "description", 12341234L, 1243)
        val todoItemUpdated =
            ToDoData(1, "updatedTitle", Priority.HIGH, "UpdatedDescription", 123411234L, 1243)

        dao.insertData(todoItem)
        dao.updateData(todoItemUpdated)

        val todoList = dao.getAllData().getOrAwaitValue()

        assertThat(todoList).containsExactly(todoItemUpdated)
        assertThat(todoList).doesNotContain(todoItem)
    }
}