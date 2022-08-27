package com.connorb26.notesapp.feature_note.presentation.calendar.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.connorb26.notesapp.feature_note.domain.model.Event
import com.connorb26.notesapp.ui.theme.LightGray

@Composable
fun EventItem(
    event: Event,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit
) {
    Card(
        backgroundColor = LightGray,
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = event.title,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.weight(1f))

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