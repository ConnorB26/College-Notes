package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.connorb26.notesapp.ui.theme.LightGray

@Composable
fun BlackTextFieldIcon(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    iconDesc: String
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = LightGray,
        backgroundColor = LightGray.copy(alpha = 0.4f)
    )

    Box(
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = singleLine,
                textStyle = textStyle.copy(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true
                ),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.White,
                    backgroundColor = Color.Transparent,
                    textColor = Color.White
                ),
                placeholder = { Text(text = hint, style = textStyle, color = Color.DarkGray) },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconDesc,
                        tint = Color.White
                    )
                }
            )
        }
    }
}