package com.attendance.academic_courses.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendance.academic_courses.data.Course
import com.attendance.academic_courses.data.sampleCourses
import com.attendance.academic_courses.ui.components.CourseCard
import com.attendance.academic_courses.ui.theme.CourseAppTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    courses: List<Course>,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    // State to control the staggered animation of list items
    var visibleItems by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        // Reset visible items when the screen is first composed
        visibleItems = 0
        // Animate in items one by one
        val itemCount = courses.size
        repeat(itemCount) {
            delay(100) // Delay between each item animation
            visibleItems = it + 1
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Courses",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        // Add bottom content for the theme switch
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = if (isDarkTheme)
                        Color(0xFF1E3B2F) // Dark green background for dark mode
                    else
                        Color(0xFFE8F5E9), // Light green background for light mode
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Light Mode",
                            tint = if (isDarkTheme)
                                Color(0xFFA5D6A7) // Light green for dark mode
                            else
                                Color(0xFF2E7D32), // Dark green for light mode
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onThemeToggle() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF2E7D32), // Dark green for thumb when checked
                                checkedTrackColor = Color(0xFFA5D6A7), // Light green for track when checked
                                checkedBorderColor = Color(0xFF2E7D32), // Dark green border when checked
                                uncheckedThumbColor = Color(0xFF81C784), // Medium green for thumb when unchecked
                                uncheckedTrackColor = Color(0xFFE8F5E9), // Very light green for track when unchecked
                                uncheckedBorderColor = Color(0xFF81C784) // Medium green border when unchecked
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Dark Mode",
                            tint = if (isDarkTheme)
                                Color(0xFFA5D6A7) // Light green for dark mode
                            else
                                Color(0xFF2E7D32), // Dark green for light mode
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (courses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No courses available",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 80.dp // Add extra padding at the bottom to avoid content being hidden by the switch
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(courses) { index, course ->
                    AnimatedVisibility(
                        visible = index < visibleItems,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                                slideInVertically(
                                    animationSpec = tween(durationMillis = 300),
                                    initialOffsetY = { it * 2 } // slide from 2x the height
                                )
                    ) {
                        CourseCard(course = course)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseListScreenPreview() {
    CourseAppTheme(darkTheme = false) {
        CourseListScreen(
            courses = sampleCourses,
            isDarkTheme = false,
            onThemeToggle = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CourseListScreenDarkPreview() {
    CourseAppTheme(darkTheme = true) {
        CourseListScreen(
            courses = sampleCourses,
            isDarkTheme = true,
            onThemeToggle = {}
        )
    }
}
