package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
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
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder.Companion.addHour
import com.connorb26.notesapp.ui.theme.DarkGray
import com.connorb26.notesapp.ui.theme.Gray

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ClassTimeItem(
    startTimeValue: TimeHolder? = null,
    endTimeValue: TimeHolder? = null,
    selectedDay: String? = null,
    locationValue: String? = null,
    onDayValueChange: (String) -> Unit,
    onStartTimeValueChange: (TimeHolder) -> Unit,
    onEndTimeValueChange: (TimeHolder) -> Unit,
    onLocationValueChange: (String) -> Unit
) {
    val context = LocalContext.current

    val dayOptions = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var dayExpanded by remember { mutableStateOf(false) }
    var selectedDayText by remember { mutableStateOf(selectedDay ?: "") }

    val calendar = Calendar.getInstance()
    val curHour = calendar[Calendar.HOUR_OF_DAY]
    val curMinute = calendar[Calendar.MINUTE]

    val startTimeHolderDefault = "Start Time"
    val startTimeHolder = remember { mutableStateOf(startTimeValue ?: TimeHolder.createDefault(startTimeHolderDefault)) }
    val endTimeHolderDefault = "End Time"
    val endTimeHolder = remember { mutableStateOf(endTimeValue ?: TimeHolder.createDefault(endTimeHolderDefault)) }

    val location = remember { mutableStateOf(locationValue ?: "") }

    val defaultText = "Tap to edit class time details"
    val displayText = remember { mutableStateOf(
        if(selectedDay!!.isBlank() || startTimeValue == null || endTimeValue == null)
            defaultText
        else
            getUpdatedDisplayText(selectedDayText, startTimeHolder.value, endTimeHolder.value, location.value)
        )
    }

    val openDialog = remember { mutableStateOf(false) }

    val startTimePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            startTimeHolder.value = TimeHolder.create(hour, minute)
            onStartTimeValueChange(startTimeHolder.value)
        }, curHour, curMinute, false
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            endTimeHolder.value = TimeHolder.create(hour, minute)
            onEndTimeValueChange(endTimeHolder.value)
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
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    openDialog.value = true
                }
        ) {
            Text(
                text = displayText.value,
                color = if(displayText.value == defaultText) Gray else Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    if(openDialog.value) {
        NoPaddingAlertDialog(
            onDismissRequest = {
                openDialog.value = false

                if(TimeHolder.compare(startTimeHolder.value, endTimeHolder.value)) {
                    endTimeHolder.value = addHour(startTimeHolder.value)
                }

                if(selectedDayText.isNotEmpty() && startTimeHolder.value.toString() != startTimeHolderDefault && endTimeHolder.value.toString() != endTimeHolderDefault) {
                    displayText.value = getUpdatedDisplayText(selectedDayText, startTimeHolder.value, endTimeHolder.value, location.value)
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
                        ExposedDropdownMenuBox(
                            expanded = dayExpanded,
                            onExpandedChange = {
                                dayExpanded = !dayExpanded
                            }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedDayText,
                                onValueChange = { },
                                placeholder = {
                                    Text(
                                        text = "Day",
                                        color = Color.DarkGray,
                                        textAlign = TextAlign.Center
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = dayExpanded
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(textColor = Color.White),
                                shape = RectangleShape,
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = dayExpanded,
                                onDismissRequest = {
                                    dayExpanded = false
                                },
                                modifier = Modifier
                                    .background(DarkGray)
                            ) {
                                dayOptions.forEach { selectionOption ->
                                    val opt: String = selectionOption
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedDayText = opt
                                            dayExpanded = false
                                            onDayValueChange(selectedDayText)
                                        }
                                    ) {
                                        Text(
                                            text = opt,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Location",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                        )
                        CustomTextField(
                            text = location.value,
                            hint = "Location",
                            onValueChange = {
                                location.value = it
                                onLocationValueChange(location.value)
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
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = Color.White,
                            modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                        )

                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .clickable {
                                    startTimePickerDialog.show()
                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RectangleShape
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = startTimeHolder.value.toString(),
                                color = if (startTimeHolder.value.toString() == startTimeHolderDefault) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                        Text(text = "-", color = Color.White)
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .clickable {
                                    endTimePickerDialog.show()
                                }
                                .background(
                                    color = Color.Transparent,
                                    shape = RectangleShape
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = endTimeHolder.value.toString(),
                                color = if (endTimeHolder.value.toString() == endTimeHolderDefault) Color.DarkGray else Color.White,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.padding(horizontal = 16.dp)
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

private fun getUpdatedDisplayText(day: String, startTime: TimeHolder, endTime: TimeHolder, location: String): String {
    val newDay = if(day.length >= 3) day.substring(0, 3) else day
    return if(location.isBlank()) {
        "$newDay at $startTime - $endTime"
    }
    else {
        "$newDay at $startTime - $endTime in $location"
    }
}