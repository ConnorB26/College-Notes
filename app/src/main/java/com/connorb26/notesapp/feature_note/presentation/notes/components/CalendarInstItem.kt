package com.connorb26.notesapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.connorb26.notesapp.feature_note.domain.model.CalendarInst

@Composable
fun CalendarInstItem(
    calInst: CalendarInst,
    selectedID: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = calInst.id == selectedID,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.onBackground
            )
        )
        Text(
            text = calInst.emailAddress,
            style = TextStyle(fontSize = 14.sp),
            color = Color.White
        )
    }
}