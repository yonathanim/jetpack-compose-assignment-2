package com.example.todoapp.data.repository

import com.example.todoapp.data.local.TodoDao
import com.example.todoapp.data.model.Todo
import com.example.todoapp.data.remote.TodoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApiService: TodoApiService,
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<List<Todo>> = flow {
        // Emit cached data first
        emitAll(todoDao.getAllTodos())
        
        try {
            // Fetch fresh data from API
            val remoteTodos = todoApiService.getTodos()
            // Update cache
            todoDao.insertTodos(remoteTodos)
        } catch (e: Exception) {
            // If we already emitted cached data, the UI will show that
            // If there's no cached data, this will just complete without emitting
        }
    }
    
    fun getTodoById(id: Int): Flow<Todo> = flow {
        // Emit cached data first
        emitAll(todoDao.getTodoById(id))
        
        try {
            // Fetch fresh data from API
            val remoteTodo = todoApiService.getTodoById(id)
            // Update cache
            todoDao.insertTodo(remoteTodo)
        } catch (e: Exception) {
            // If we already emitted cached data, the UI will show that
        }
    }
}
