package com.connorb26.notesapp.feature_note.presentation.classes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.connorb26.notesapp.feature_note.domain.model.Class

@Composable
fun ClassItem(
    classObj: Class,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    color: Color = Color.Black
) {
    Card(
        modifier = modifier,
        backgroundColor = color,
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = classObj.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )

            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Class",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}