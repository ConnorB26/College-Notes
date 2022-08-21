package com.connorb26.notesapp.feature_note.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.connorb26.notesapp.ui.theme.*

@Composable
fun CustomBasicTextField(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = { },
    backgroundColor: Color = Color.Transparent,
    selectionColor: Color = LightGray,
    textColor: Color = Color.White,
    borderColor: Color = DarkestGray,
    readOnly: Boolean = false,
    readOnlyColor: Color = Color.Gray,
    readOnlyAction: () -> Unit = { }
) {
    val customTextSelectionColors = TextSelectionColors(
        handleColor = selectionColor,
        backgroundColor = selectionColor.copy(alpha = 0.4f)
    )

    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = singleLine,
                readOnly = readOnly,
                enabled = !readOnly,
                textStyle = textStyle.copy(color = if(!readOnly) textColor else readOnlyColor),
                modifier = modifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .then(
                        if(readOnly) {
                            Modifier.clickable(onClick = readOnlyAction)
                        }
                        else Modifier
                    ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(selectionColor)
            )
        }
    }
}