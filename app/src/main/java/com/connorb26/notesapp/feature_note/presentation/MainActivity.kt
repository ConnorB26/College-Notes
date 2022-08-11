package com.connorb26.notesapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.connorb26.notesapp.feature_note.presentation.calendar.CalendarScreen
import com.connorb26.notesapp.feature_note.presentation.notes.NotesScreen
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.ui.theme.NotesAppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesAppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.CalendarScreen.route,
                            enterTransition = { _, _ ->
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                            },
                            exitTransition = { _, _ ->
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                            }
                        ) {
                            CalendarScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            ),
                            enterTransition = { _, _ ->
                                slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                            },
                            exitTransition = { _, _ ->
                                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                            }
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}
