package com.connorb26.notesapp.feature_note.presentation.notes

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.domain.model.CalendarInst.Companion.selCalPrefName
import com.connorb26.notesapp.feature_note.presentation.notes.components.CalendarInstItem
import com.connorb26.notesapp.feature_note.presentation.notes.components.NoteItem
import com.connorb26.notesapp.feature_note.presentation.notes.components.OrderSection
import com.connorb26.notesapp.feature_note.presentation.util.DeleteDialog
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.feature_note.presentation.util.VariableColor
import com.connorb26.notesapp.ui.theme.ActionBlue
import com.connorb26.notesapp.ui.theme.DarkGray
import com.connorb26.notesapp.ui.theme.Gray
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
    val calendarInstances = viewModel.calendarInstances.value


    val activity = LocalContext.current as? Activity
    BackHandler {
        activity?.finish()
    }

    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    val selectedCalendarID = remember { mutableStateOf(1L) }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                viewModel.onEvent(NotesEvent.GetCalendars)
                selectedCalendarID.value = sharedPref?.getLong(selCalPrefName, 1) ?: 1
            }
            else -> {}
        }
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
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerBackgroundColor = Gray,
        drawerShape = RoundedCornerShape(10.dp),
        drawerContent = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected Calendar",
                    style = MaterialTheme.typography.h5,
                    color = Color.White
                )
            }

            LazyColumn(
                modifier = Modifier
            ) {
                items(calendarInstances) { calInst ->
                    CalendarInstItem(
                        calInst = calInst,
                        selectedID = selectedCalendarID.value,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            with (sharedPref?.edit()) {
                                this?.putLong(selCalPrefName, calInst.id)
                                this?.apply()
                            }
                            selectedCalendarID.value = calInst.id
                        }
                    )
                }
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
                        viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
                IconButton(
                    onClick = {
                        viewModel.onEvent(NotesEvent.NavigateToCalendar(navController))
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar Screen",
                        tint = if(viewModel.checkPermission()) Color.White else Gray
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
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
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

@Composable
private fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}