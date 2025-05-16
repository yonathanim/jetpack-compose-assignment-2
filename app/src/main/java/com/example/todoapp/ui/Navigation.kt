package com.example.todoapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.ui.screens.TodoDetailScreen
import com.example.todoapp.ui.screens.TodoListScreen

@Composable
fun TodoNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "todos"
    ) {
        composable("todos") {
            TodoListScreen(
                onTodoClick = { todoId ->
                    navController.navigate("todos/$todoId")
                }
            )
        }
        composable(
            route = "todos/{todoId}",
            arguments = listOf(
                navArgument("todoId") { type = NavType.IntType }
            )
        ) {
            TodoDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
