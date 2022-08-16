package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.connorb26.notesapp.R
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import java.util.*

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
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    calendar.time = Date()

    var examName = remember { mutableStateOf(nameValue ?: "") }
    val dateHolderDefault = "Date"
    val dateHolder = remember { mutableStateOf(dateValue ?: DateHolder(default = dateHolderDefault)) }
    val timeHolderDefault = "Time"
    val timeHolder = remember { mutableStateOf(timeValue ?: TimeHolder(default = timeHolderDefault)) }

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
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Dialog,
        {_, hour: Int, minute: Int ->
            timeHolder.value = TimeHolder(
                hour = (if(hour - 12 <= 0) hour else (hour - 12)).toString(),
                minute = if(minute / 10 < 1) "0$minute" else minute.toString(),
                ampm = if(hour - 12 < 0) "AM" else "PM"
            )
            onTimeValueChange(timeHolder.value)
        }, hour, minute, false
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BlackTextField(
            text = examName.value,
            hint = "Exam Name",
            onValueChange = {
                examName.value = it
                onNameValueChange(it)
            },
            modifier = Modifier
                .height(50.dp)
                .weight(0.9f)
        )

        Button(
            onClick = { datePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            shape = RectangleShape,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            Text(
                text = dateHolder.value.toString(),
                color = if (dateHolder.value.toString() == dateHolderDefault) Color.DarkGray else Color.White,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = { timePickerDialog.show() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
            shape = RectangleShape,
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
        ) {
            Text(
                text = timeHolder.value.toString(),
                color = if (timeHolder.value.toString() == timeHolderDefault) Color.DarkGray else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}