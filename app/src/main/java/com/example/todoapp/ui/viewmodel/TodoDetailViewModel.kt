package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class TodoDetailViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val todoId: Int = checkNotNull(savedStateHandle["todoId"])
    
    private val _uiState = MutableStateFlow<TodoDetailUiState>(TodoDetailUiState.Loading)
    val uiState: StateFlow<TodoDetailUiState> = _uiState
    
    init {
        loadTodo()
    }
    
    fun loadTodo() {
        viewModelScope.launch {
            _uiState.value = TodoDetailUiState.Loading
            todoRepository.getTodoById(todoId)
                .catch { e ->
                    _uiState.value = TodoDetailUiState.Error(e.message ?: "Unknown error")
                }
                .collect { todo ->
                    _uiState.value = TodoDetailUiState.Success(todo)
                }
        }
    }
}

sealed class TodoDetailUiState {
    object Loading : TodoDetailUiState()
    data class Success(val todo: Todo) : TodoDetailUiState()
    data class Error(val message: String) : TodoDetailUiState()
}
