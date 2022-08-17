package com.connorb26.notesapp.feature_note.presentation.add_edit_class.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.connorb26.notesapp.ui.theme.Blue
import com.connorb26.notesapp.ui.theme.Green
import com.connorb26.notesapp.ui.theme.LightGray

@Composable
fun BlackTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    iconDesc: String = "",
    readOnly: Boolean = false,
    readOnlyColor: Color = Color.Gray
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
                textStyle = textStyle.copy(color = if(readOnly) readOnlyColor else Color.White),
                readOnly = readOnly,
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
                    textColor = if(readOnly) readOnlyColor else Color.White
                ),
                placeholder = { Text(text = hint, style = textStyle, color = Color.DarkGray) },
                leadingIcon = if(icon != null) leadingIconComp(icon, iconDesc) else null
            )
        }
    }
}

private fun leadingIconComp(
    icon: ImageVector,
    iconDesc: String
) = @Composable {
    Icon(
        imageVector = icon,
        contentDescription = iconDesc,
        tint = Color.White
    )
}