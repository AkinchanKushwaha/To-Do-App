package com.example.todo.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TodoDatabase
import com.example.todo.data.models.ToDoData
import com.example.todo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = TodoDatabase.getDataBase(application).todoDao()
    private val repository: TodoRepository

    private val getAllData: LiveData<List<ToDoData>>

    init {
        repository = TodoRepository(toDoDao)
        getAllData = repository.getAllData
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }
}