package com.connorb26.notesapp.feature_note.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.connorb26.notesapp.ui.theme.Blue
import com.connorb26.notesapp.ui.theme.DarkGray

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeleteDialog(
    deleteMessage: AnnotatedString,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    height: Int
) {
    NoPaddingAlertDialog(
        onDismissRequest = onDismiss,
        text = {
           Text(
               text = deleteMessage,
               modifier = Modifier.padding(16.dp)
           )
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        dismissButton = {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(45.dp)
                    .background(Color.Transparent)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel",
                    color = Blue
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(45.dp)
                    .background(Color.Transparent)
                    .clickable(onClick = onConfirm),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Confirm",
                    color = Blue
                )
            }
        },
        modifier = Modifier
            .width(260.dp)
            .height(height.dp)
            .clip(RoundedCornerShape(15.dp)),
        backgroundColor = DarkGray
    )
}