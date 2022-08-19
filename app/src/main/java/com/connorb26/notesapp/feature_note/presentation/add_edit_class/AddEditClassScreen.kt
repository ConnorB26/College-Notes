package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.connorb26.notesapp.R
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.*
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.feature_note.presentation.util.scrollbar
import com.connorb26.notesapp.ui.theme.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditClassScreen(
    navController: NavController,
    viewModel: AddEditClassViewModel = hiltViewModel(),
    className: String
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    val colorPickerEnabled = remember { mutableStateOf(false) }
    val fieldLabelColor = Blue2
    val fieldLabelOffset = Modifier.offset(x = 6.dp)

    val classState = viewModel.classState.value
    val classTimes = viewModel.classTimes.value
    var firstDay = viewModel.firstDay.value
    var lastDay = viewModel.lastDay.value
    val exams = viewModel.exams.value

    val calendar = Calendar.getInstance()

    val firstDayDatePickerDialog = DatePickerDialog(
        context,
        R.style.Theme_Dialog,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            firstDay = DateHolder.create(year, month, day)
            if(DateHolder.compare(firstDay, lastDay)) {
                lastDay = DateHolder.addDay(firstDay)
                viewModel.onEvent(AddEditClassEvent.EnteredLastDay(lastDay))
            }
            viewModel.onEvent(AddEditClassEvent.EnteredFirstDay(firstDay))
        },
        if(firstDay.year == 0) calendar.get(Calendar.YEAR) else firstDay.year,
        if(firstDay.month == 0) calendar.get(Calendar.MONTH) else firstDay.month,
        if(firstDay.day == 0) calendar.get(Calendar.DAY_OF_MONTH) else firstDay.day
    )

    val lastDayDatePickerDialog = DatePickerDialog(
        context,
        R.style.Theme_Dialog,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            lastDay = DateHolder.create(year, month, day)
            if(DateHolder.compare(firstDay, lastDay)) {
                lastDay = DateHolder.addDay(firstDay)
            }
            viewModel.onEvent(AddEditClassEvent.EnteredLastDay(lastDay))
        },
        if(lastDay.year == 0) calendar.get(Calendar.YEAR) else lastDay.year,
        if(lastDay.month == 0) calendar.get(Calendar.MONTH) else lastDay.month,
        if(lastDay.day == 0) calendar.get(Calendar.DAY_OF_MONTH) else lastDay.day
    )

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
        if(colorPickerEnabled.value) {
            ColorPickerDialog(
                onSave = {
                    colorPickerEnabled.value = false
                    viewModel.onEvent(AddEditClassEvent.EnteredColor(it.toArgb()))
                },
                onDismiss = { colorPickerEnabled.value = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Title,
                    contentDescription = "Name",
                    tint = fieldLabelColor,
                    modifier = Modifier
                        .size(38.dp)
                        .padding(end = 10.dp)
                        .offset(y = 10.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Class Name",
                        color = fieldLabelColor,
                        modifier = fieldLabelOffset
                    )
                    CustomBasicTextField(
                        text = classState.name,
                        onValueChange = {
                            viewModel.onEvent(AddEditClassEvent.EnteredName(it))
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.h5,
                        readOnly = className.isNotBlank(),
                        readOnlyColor = Color.LightGray,
                        backgroundColor = DarkIshGray,
                        readOnlyAction = {
                            viewModel.onEvent(AddEditClassEvent.ShowSnackbarMessage("Class name can't be edited"))
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = "Color",
                    tint = fieldLabelColor,
                    modifier = Modifier
                        .size(38.dp)
                        .padding(end = 10.dp)
                        .offset(y = 10.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Color",
                        color = fieldLabelColor,
                        modifier = fieldLabelOffset
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable { colorPickerEnabled.value = true }
                            .background(
                                color = Color(classState.color),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = DarkestGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Class Date Range",
                    tint = fieldLabelColor,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = 10.dp)
                        .offset(y = 10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(65.dp)
                            .fillMaxWidth(0.5f)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Start Date",
                            color = fieldLabelColor,
                            modifier = fieldLabelOffset
                        )
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    firstDayDatePickerDialog.show()
                                }
                                .background(
                                    color = DarkIshGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = DarkestGray,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = firstDay.toString(),
                                color = if (firstDay.toString() == firstDay.default) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Text(
                        text = "-",
                        color = fieldLabelColor,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .offset(y = 10.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(65.dp)
                            .fillMaxWidth(0.5f)
                            .weight(1f)
                    ) {
                        Text(
                            text = "End Date",
                            color = fieldLabelColor,
                            modifier = fieldLabelOffset
                        )
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    lastDayDatePickerDialog.show()
                                }
                                .background(
                                    color = DarkIshGray,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    width = 2.dp,
                                    color = DarkestGray,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lastDay.toString(),
                                color = if (lastDay.toString() == lastDay.default) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().then(fieldLabelOffset),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Class Times",
                    color = fieldLabelColor
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { viewModel.onEvent(AddEditClassEvent.AddClassTime) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Class Time",
                        tint = fieldLabelColor
                    )
                }

                Text(
                    text = "/",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = fieldLabelColor
                )

                IconButton(
                    onClick = { viewModel.onEvent(AddEditClassEvent.RemoveClassTime) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Remove Class Time",
                        tint = fieldLabelColor
                    )
                }
            }

            val listState1 = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f)
                    .offset(y = (-12).dp)
                    .background(
                        color = DarkIshGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = DarkestGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(shape = RoundedCornerShape(10.dp))
                    .then(
                        if (classTimes.size > 2) {
                            Modifier.scrollbar(
                                state = listState1,
                                horizontal = false,
                                knobColor = Color.White,
                                trackColor = DarkGray,
                                hiddenAlpha = 0.5f,
                                padding = 10.dp,
                                thickness = 8.dp,
                                xEndOffset = 8.dp
                            )
                        }
                        else Modifier
                    ),
                state = listState1
            ) {
                items(classTimes) { classTime ->
                    ClassTimeItem(
                        startTimeValue = classTime.startTime,
                        endTimeValue  = classTime.endTime,
                        selectedDay = classTime.dayOfWeek,
                        locationValue = classTime.location,
                        onDayValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassDay(classTime, it)) },
                        onStartTimeValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassStartTime(classTime, it)) },
                        onEndTimeValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassEndTime(classTime, it)) },
                        onLocationValueChange = { viewModel.onEvent(AddEditClassEvent.EnteredClassLocation(classTime, it)) }
                    )
                    Divider(
                        color = Gray,
                        thickness = 2.dp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().then(fieldLabelOffset),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exams",
                    color = fieldLabelColor
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { viewModel.onEvent(AddEditClassEvent.AddExam) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Exam",
                        tint = fieldLabelColor
                    )
                }

                Text(
                    text = "/",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = fieldLabelColor
                )

                IconButton(
                    onClick = { viewModel.onEvent(AddEditClassEvent.RemoveExam) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Remove Exam",
                        tint = fieldLabelColor
                    )
                }
            }

            val listState2 = rememberLazyListState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .offset(y = (-12).dp)
                    .background(
                        color = DarkIshGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = DarkestGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(shape = RoundedCornerShape(10.dp))
                    .then(
                        if (exams.size > 2) {
                            Modifier.scrollbar(
                                state = listState2,
                                horizontal = false,
                                knobColor = Color.White,
                                trackColor = DarkGray,
                                hiddenAlpha = 0.5f,
                                padding = 10.dp,
                                thickness = 8.dp,
                                xEndOffset = 8.dp
                            )
                        }
                        else Modifier
                    ),
                state = listState2
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
                    Divider(
                        color = Gray,
                        thickness = 2.dp
                    )
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