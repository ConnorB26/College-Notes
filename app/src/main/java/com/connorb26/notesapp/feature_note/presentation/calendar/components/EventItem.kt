package com.connorb26.notesapp.feature_note.presentation.calendar.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.connorb26.notesapp.feature_note.domain.model.Event

@Composable
fun EventItem(
    event: Event,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit
) {
    Card(
        //border = BorderStroke(2.dp, Color.LightGray),
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onEditClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit event",
                    tint = MaterialTheme.colors.secondary,
                )
            }
        }
    }
}