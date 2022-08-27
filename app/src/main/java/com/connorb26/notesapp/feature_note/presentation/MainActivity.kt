package com.connorb26.notesapp.feature_note.presentation

import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.AddEditClassScreen
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.connorb26.notesapp.feature_note.presentation.calendar.CalendarScreen
import com.connorb26.notesapp.feature_note.presentation.classes.ClassesScreen
import com.connorb26.notesapp.feature_note.presentation.notes.NotesScreen
import com.connorb26.notesapp.feature_note.presentation.util.LockScreenOrientation
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.ui.theme.DarkGray
import com.connorb26.notesapp.ui.theme.NotesAppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR), 42)
        window.navigationBarColor = DarkGray.toArgb()
        window.statusBarColor = DarkGray.toArgb()
        setContent {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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
                            enterTransition = {
                                when(targetState.destination.route) {
                                    Screen.CalendarScreen.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when(targetState.destination.route) {
                                    Screen.NotesScreen.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                                    else -> null
                                }
                            }
                        ) {
                            CalendarScreen(navController = navController)
                        }
                        composable(
                            route = Screen.ClassScreen.route,
                            enterTransition = {
                                when(initialState.destination.route) {
                                    Screen.NotesScreen.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when(targetState.destination.route) {
                                    Screen.NotesScreen.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                                    else -> null
                                }
                            }
                        ) {
                            ClassesScreen(navController = navController)
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
                            enterTransition = {
                                when(initialState.destination.route) {
                                    Screen.NotesScreen.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(500))
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when(targetState.destination.route) {
                                    Screen.NotesScreen.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(500))
                                    else -> null
                                }
                            }
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )
                        }
                        composable(
                            route = Screen.AddEditClassScreen.route +
                                    "?classId={classId}",
                            arguments = listOf(
                                navArgument(
                                    name = "classId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            ),
                            enterTransition = {
                                when(initialState.destination.route) {
                                    Screen.ClassScreen.route -> slideIntoContainer(AnimatedContentScope.SlideDirection.Up, animationSpec = tween(500))
                                    else -> null
                                }
                            },
                            exitTransition = {
                                when(targetState.destination.route) {
                                    Screen.ClassScreen.route -> slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(500))
                                    else -> null
                                }
                            }
                        ) {
                            AddEditClassScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}