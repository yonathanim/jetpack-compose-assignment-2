package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<TodoListUiState>(TodoListUiState.Loading)
    val uiState: StateFlow<TodoListUiState> = _uiState
    
    init {
        loadTodos()
    }
    
    fun loadTodos() {
        viewModelScope.launch {
            _uiState.value = TodoListUiState.Loading
            todoRepository.getTodos()
                .catch { e ->
                    _uiState.value = TodoListUiState.Error(e.message ?: "Unknown error")
                }
                .collect { todos ->
                    _uiState.value = if (todos.isEmpty()) {
                        TodoListUiState.Empty
                    } else {
                        TodoListUiState.Success(todos)
                    }
                }
        }
    }
}

sealed class TodoListUiState {
    object Loading : TodoListUiState()
    data class Success(val todos: List<Todo>) : TodoListUiState()
    object Empty : TodoListUiState()
    data class Error(val message: String) : TodoListUiState()
}
