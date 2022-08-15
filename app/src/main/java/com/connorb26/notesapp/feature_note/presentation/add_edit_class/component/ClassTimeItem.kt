package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClassTimeItem() {
    val dayOptions = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val startTime = remember { mutableStateOf("") }
    val endTime = remember { mutableStateOf("") }

    val startTimePickerDialog = TimePickerDialog(
        context,
        {_, hour: Int, minute: Int ->
            startTime.value = "$hour:$minute"
        }, hour, minute, false
    )

    val endTimePickerDialog = TimePickerDialog(
        context,
        {_, hour: Int, minute: Int ->
            endTime.value = "$hour:$minute"
        }, hour, minute, false
    )


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                placeholder = { Text("Day of Week") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                dayOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }

        Button(
            onClick = { startTimePickerDialog.show() }
        ) {
            Text(text = startTime.value)
        }

        Button(
            onClick = { endTimePickerDialog.show() }
        ) {
            Text(text = endTime.value)
        }
    }
}