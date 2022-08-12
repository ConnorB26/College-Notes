package com.connorb26.notesapp.feature_note.presentation.calendar.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.connorb26.notesapp.feature_note.domain.util.Event

@Composable
fun EventItem(
    event: Event,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    onEditClick: () -> Unit
) {
    IconButton(
        onClick = onEditClick
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit event",
            tint = MaterialTheme.colors.primary,
        )
    }
}