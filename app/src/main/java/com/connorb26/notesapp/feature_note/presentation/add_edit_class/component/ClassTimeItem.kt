package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.connorb26.notesapp.R
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClassTimeItem(
    startTimeValue: TimeHolder? = null,
    endTimeValue: TimeHolder? = null,
    selectedDay: String? = null,
    onDayValueChange: (String) -> Unit,
    onStartTimeValueChange: (TimeHolder) -> Unit,
    onEndTimeValueChange: (TimeHolder) -> Unit
) {
    val context = LocalContext.current

    val dayOptions = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var dayExpanded by remember { mutableStateOf(false) }
    var selectedDayText by remember { mutableStateOf(selectedDay ?: "") }

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val startTimeHolderDefault = "Start Time"
    val startTimeHolder = remember { mutableStateOf(startTimeValue ?: TimeHolder(default = startTimeHolderDefault)) }
    val endTimeHolderDefault = "End Time"
    val endTimeHolder = remember { mutableStateOf(endTimeValue ?: TimeHolder(default = endTimeHolderDefault)) }

    val startTimePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            startTimeHolder.value = TimeHolder(
                hour = (if(hour - 12 <= 0) hour else (hour - 12)).toString(),
                minute = if(minute / 10 < 1) "0$minute" else minute.toString(),
                ampm = if(hour - 12 < 0) "AM" else "PM"
            )
            onStartTimeValueChange(startTimeHolder.value)
        }, hour, minute, false
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            endTimeHolder.value = TimeHolder(
                hour = (if(hour - 12 <= 0) hour else (hour - 12)).toString(),
                minute = if(minute / 10 < 1) "0$minute" else minute.toString(),
                ampm = if(hour - 12 < 0) "AM" else "PM"
            )
            onEndTimeValueChange(endTimeHolder.value)
        }, hour, minute, false
    )


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = dayExpanded,
            onExpandedChange = {
                dayExpanded = !dayExpanded
            },
            modifier = Modifier
                .weight(0.9f)
        ) {
            TextField(
                readOnly = true,
                value = if(selectedDayText.length >= 3) selectedDayText.substring(0,3) else "",
                onValueChange = { },
                placeholder = { Text(text = "Day", color = Color.DarkGray, textAlign = TextAlign.Center) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dayExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.Black),
                shape = RectangleShape,
                modifier = Modifier
                    .height(50.dp)
            )
            ExposedDropdownMenu(
                expanded = dayExpanded,
                onDismissRequest = {
                    dayExpanded = false
                }
            ) {
                dayOptions.forEach { selectionOption ->
                    val opt: String = selectionOption.substring(0, 3)
                    DropdownMenuItem(
                        onClick = {
                            selectedDayText = opt
                            dayExpanded = false
                            onDayValueChange(selectedDayText)
                        }
                    ) {
                        Text(text = opt)
                    }
                }
            }
        }

        Button(
            onClick = { startTimePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            shape = RectangleShape,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            Text(
                text = startTimeHolder.value.toString(),
                color = if (startTimeHolder.value.toString() == startTimeHolderDefault) Color.DarkGray else Color.White,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { endTimePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            shape = RectangleShape,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            Text(
                text = endTimeHolder.value.toString(),
                color = if (endTimeHolder.value.toString() == endTimeHolderDefault) Color.DarkGray else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}