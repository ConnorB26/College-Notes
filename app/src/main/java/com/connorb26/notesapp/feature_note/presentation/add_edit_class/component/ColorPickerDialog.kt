package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.connorb26.notesapp.ui.theme.DarkGray
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    onColorChanged: (Color) -> Unit
) {
    val controller = rememberColorPickerController()
    controller.setDebounceDuration(100L)
    controller.setWheelColor(Color.Black)

    NoPaddingAlertDialog(
        onDismissRequest = onDismiss,
        text = {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    onColorChanged(colorEnvelope.color)
                }
            )
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(300.dp)
            .clip(RoundedCornerShape(15.dp)),
        backgroundColor = DarkGray
    )
}