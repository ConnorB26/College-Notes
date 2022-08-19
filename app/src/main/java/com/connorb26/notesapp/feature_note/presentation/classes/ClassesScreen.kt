package com.connorb26.notesapp.feature_note.presentation.classes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.presentation.classes.components.ClassItem
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun ClassesScreen(
    navController: NavController,
    viewModel: ClassesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    BackHandler {
        navController.navigate(Screen.NotesScreen.route)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = Color(0xff00d8db),
                    snackbarData = data
                )
            }
        }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.NotesScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Classes",
                        style = MaterialTheme.typography.h5
                    )

                    IconButton(
                        onClick = {
                            navController.navigate(Screen.AddEditClassScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add class",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(state.classes) { classObj ->
                    ClassItem(
                        classObj = classObj,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditClassScreen.route +
                                            "?className=${classObj.name}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(ClassesEvent.DeleteClass(classObj))
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Class deleted"
                                )
                            }
                        },
                        color = Color(classObj.color)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}