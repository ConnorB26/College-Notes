package com.connorb26.notesapp.feature_note.presentation.notes

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.presentation.classes.ClassesEvent
import com.connorb26.notesapp.feature_note.presentation.notes.components.NoteItem
import com.connorb26.notesapp.feature_note.presentation.notes.components.OrderSection
import com.connorb26.notesapp.feature_note.presentation.util.DeleteDialog
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.feature_note.presentation.util.VariableColor
import com.connorb26.notesapp.ui.theme.ActionBlue
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val deleteDialog = viewModel.deleteDialog.value

    val activity = (LocalContext.current as? Activity)
    BackHandler {
        activity?.finish()
    }

    if(deleteDialog) {
        DeleteDialog(
            deleteMessage =
            buildAnnotatedString {
                append("Are you sure you want to delete '")
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = VariableColor.clampLuminance(Color(viewModel.deletingNote!!.color)))
                ) {
                    append(viewModel.deletingNote!!.title)
                }
                append("'?")
            },
            onConfirm = {
                viewModel.onEvent(NotesEvent.DeleteNote(viewModel.deletingNote!!))
                scope.launch {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Note deleted",
                        actionLabel = "Undo"
                    )
                    if(result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
                viewModel.onEvent(NotesEvent.DeleteDialogDisable)
            },
            onDismiss = {
                viewModel.onEvent(NotesEvent.DeleteDialogDisable)
            },
            height = 125
        )
    }

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddEditNoteScreen.route)
            },
            backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note"
                )
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = ActionBlue,
                    snackbarData = data
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.h4
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        navController.navigate(Screen.CalendarScreen.route)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar Screen"
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(Screen.ClassScreen.route)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Class,
                        contentDescription = "Class Screen"
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditNoteScreen.route +
                                            "?noteId=${note.id}&noteColor=${note.color}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteDialogEnable(note))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}