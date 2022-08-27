package com.connorb26.notesapp.feature_note.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.connorb26.notesapp.feature_note.presentation.util.NoPaddingAlertDialog
import com.connorb26.notesapp.ui.theme.DarkGray
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerDialog(
    onSave: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()

    val color = remember { mutableStateOf(Color.White) }

    NoPaddingAlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(0.6f)
                        .background(
                            color = color.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {

                }
            }
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    color.value = colorEnvelope.color
                }
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .height(20.dp),
                controller = controller,
            )
        },
        dismissButton = {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(45.dp)
                    .background(Color.Transparent)
                    .clickable{ onDismiss.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel"
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(45.dp)
                    .background(Color.Transparent)
                    .clickable { onSave(color.value) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Save"
                )
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .width(250.dp)
            .height(345.dp)
            .clip(RoundedCornerShape(15.dp)),
        backgroundColor = DarkGray
    )
}