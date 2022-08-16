package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.BlackTextField
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.BlackTextFieldIcon
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.ClassTimeItem
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.ExamItem
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.connorb26.notesapp.feature_note.presentation.notes.NotesEvent
import com.connorb26.notesapp.feature_note.presentation.notes.components.NoteItem
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditClassScreen(
    navController: NavController,
    viewModel: AddEditClassViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val classState = viewModel.classState.value
    val classTimes = viewModel.classTimes.value
    val exams = viewModel.exams.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditClassViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditClassViewModel.UiEvent.NavigateUp -> {
                    navController.navigate(Screen.ClassScreen.route)
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditClassEvent.CancelClass)
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackTextField(
                    text = classState.name,
                    hint = "Class Name",
                    onValueChange = {
                        viewModel.onEvent(AddEditClassEvent.EnteredName(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackTextFieldIcon(
                    text = classState.location,
                    hint = "Location",
                    onValueChange = {
                        viewModel.onEvent(AddEditClassEvent.EnteredLocation(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.MyLocation,
                    iconDesc = "Location"
                )
            }

            Column (

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Class Times")

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { viewModel.onEvent(AddEditClassEvent.AddClassTime) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Class Time"
                        )
                    }

                    Text(
                        text = "/",
                        fontSize = 25.sp
                    )

                    IconButton(
                        onClick = { viewModel.onEvent(AddEditClassEvent.RemoveClassTime) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Class Time"
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(Color.Black)
                ) {
                    items(classTimes) { classTime ->
                        ClassTimeItem(
                            startTimeValue = classTime.startTime,
                            endTimeValue  = classTime.endTime,
                            selectedDay = classTime.dayOfWeek,
                            onDayValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassDay(classTime, it)) },
                            onStartTimeValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassStartTime(classTime, it)) },
                            onEndTimeValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassEndTime(classTime, it)) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            Column (

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Exams")

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = { viewModel.onEvent(AddEditClassEvent.AddExam) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exam"
                        )
                    }

                    Text(
                        text = "/",
                        fontSize = 25.sp
                    )

                    IconButton(
                        onClick = { viewModel.onEvent(AddEditClassEvent.RemoveExam) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Exam"
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(Color.Black)
                ) {
                    items(exams) { exam ->
                        ExamItem(
                            nameValue = exam.name,
                            dateValue  = exam.date,
                            timeValue = exam.time,
                            onNameValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredExamName(exam, it)) },
                            onDateValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredExamDate(exam, it)) },
                            onTimeValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredExamTime(exam, it)) }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(50.dp)
                        .weight(1f)
                        .clickable { viewModel.onEvent(AddEditClassEvent.CancelClass) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colors.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(50.dp)
                        .weight(1f)
                        .clickable { viewModel.onEvent(AddEditClassEvent.SaveClass) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}