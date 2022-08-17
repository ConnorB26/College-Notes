package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.connorb26.notesapp.R
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import com.connorb26.notesapp.ui.theme.DarkGray
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExamItem(
    nameValue: String = "",
    dateValue: DateHolder? = null,
    timeValue: TimeHolder? = null,
    onNameValueChange: (String) -> Unit,
    onDateValueChange: (DateHolder) -> Unit,
    onTimeValueChange: (TimeHolder) -> Unit
) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val curYear: Int = calendar.get(Calendar.YEAR)
    val curMonth: Int = calendar.get(Calendar.MONTH)
    val curDay: Int = calendar.get(Calendar.DAY_OF_MONTH)
    val curHour = calendar[Calendar.HOUR_OF_DAY]
    val curMinute = calendar[Calendar.MINUTE]

    calendar.time = Date()

    val examName = remember { mutableStateOf(nameValue) }
    val dateHolderDefault = "Date"
    val dateHolder = remember { mutableStateOf(dateValue ?: DateHolder(default = dateHolderDefault)) }
    val timeHolderDefault = "Time"
    val timeHolder = remember { mutableStateOf(timeValue ?: TimeHolder(default = timeHolderDefault)) }

    val defaultText = "Tap to edit exam details"
    val displayText = remember { mutableStateOf(
        if(nameValue.isBlank() || dateValue == null || timeValue == null) {
            defaultText
        }
        else {
            "${examName.value} on ${dateHolder.value} at ${timeHolder.value}"
        }
    ) }

    val openDialog = remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.Theme_Dialog,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            dateHolder.value = DateHolder(
                day = day.toString(),
                month = (month+1).toString(),
                year = year.toString().substring(2,4)
            )
            onDateValueChange(dateHolder.value)
        }, curYear, curMonth, curDay
    )

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            timeHolder.value = TimeHolder(
                hour = if (hour % 12 == 0) "12" else (hour % 12).toString(),
                minute = if(minute < 10) "0$minute" else minute.toString(),
                ampm = if(hour < 12) "AM" else "PM"
            )
            onTimeValueChange(timeHolder.value)
        }, curHour, curMinute, false
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    openDialog.value = true
                }
        ) {
            Text(
                text = displayText.value,
                color = if(displayText.value == defaultText) Color.DarkGray else Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    if(openDialog.value) {
        NoPaddingAlertDialog(
            onDismissRequest = {
                openDialog.value = false

                if(examName.value.isNotEmpty() && dateHolder.value.toString() != dateHolder.value.default && timeHolder.value.toString() != timeHolder.value.default) {
                    displayText.value = "${examName.value} on ${dateHolder.value} at ${timeHolder.value}"
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Title,
                            contentDescription = "Name",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                        )
                        CustomTextField(
                            text = examName.value,
                            hint = "Exam Name",
                            onValueChange = {
                                examName.value = it
                                onNameValueChange(examName.value)
                            },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Date",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                        )
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .weight(1f)
                                .clickable {
                                    datePickerDialog.show()
                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RectangleShape
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = dateHolder.value.toString(),
                                color = if (dateHolder.value.toString() == dateHolderDefault) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                        )

                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .weight(1f)
                                .clickable {
                                    timePickerDialog.show()
                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RectangleShape
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = timeHolder.value.toString(),
                                color = if (timeHolder.value.toString() == timeHolderDefault) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(150.dp)
                .clip(RoundedCornerShape(15.dp)),
            backgroundColor = DarkGray
        )
    }
}