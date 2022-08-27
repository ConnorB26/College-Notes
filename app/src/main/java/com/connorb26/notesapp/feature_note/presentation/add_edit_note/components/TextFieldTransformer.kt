package com.connorb26.notesapp.feature_note.presentation.add_edit_note.components

import android.util.Log
import androidx.compose.material.LocalTextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp

val BOLD_REGEX = Regex(pattern = "\\\$B\\+.*\\\$B-")
val ITALICS_REGEX = Regex(pattern = "\\\$I\\+.*\\\$I-")
val UNDERLINE_REGEX = Regex(pattern = "\\\$U\\+.*\\\$U-")
val STRIKETHROUGH_REGEX = Regex(pattern = "\\\$S\\+.*\\\$S-")
val CENTER_REGEX = Regex(pattern = "\\\$C\\+.*\\\$C-")
val BULLET_NEWLINE_REGEX = Regex(pattern = "\n-")
val BULLET_START_REGEX = Regex(pattern = "^-")
const val BULLET_CHAR = '\u25CF' //\u2022

class TextFieldTransformer: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val boldMatches = BOLD_REGEX.findAll(text.text)
        val italicsMatches = ITALICS_REGEX.findAll(text.text)
        val underlineMatches = UNDERLINE_REGEX.findAll(text.text)
        val strikethroughMatches = STRIKETHROUGH_REGEX.findAll(text.text)
        val centerMatches = CENTER_REGEX.findAll(text.text)

        /*val builder = AnnotatedString.Builder(text.text
            .replace(BULLET_NEWLINE_REGEX, "\n$BULLET_CHAR")
            .replace(BULLET_START_REGEX, "$BULLET_CHAR")
        )*/
        val builder = AnnotatedString.Builder(text.text)

        for (match in boldMatches) {
            val matchRange = match.range
            builder.addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = matchRange.first, end = matchRange.last + 1)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.first, end = matchRange.first + 3)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.last - 3, end = matchRange.last + 1)
        }
        for (match in italicsMatches) {
            val matchRange = match.range
            builder.addStyle(style = SpanStyle(fontStyle = FontStyle.Italic), start = matchRange.first, end = matchRange.last + 1)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.first, end = matchRange.first + 3)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.last - 3, end = matchRange.last + 1)
        }
        for (match in underlineMatches) {
            val matchRange = match.range
            builder.addStyle(style = SpanStyle(textDecoration = TextDecoration.Underline), start = matchRange.first, end = matchRange.last + 1)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.first, end = matchRange.first + 3)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.last - 3, end = matchRange.last + 1)
        }
        for (match in strikethroughMatches) {
            val matchRange = match.range
            builder.addStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough), start = matchRange.first, end = matchRange.last + 1)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.first, end = matchRange.first + 3)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.last - 3, end = matchRange.last + 1)
        }
        for (match in centerMatches) {
            val matchRange = match.range
            builder.addStyle(style = ParagraphStyle(textAlign = TextAlign.Center), start = matchRange.first, end = matchRange.last + 1)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.first, end = matchRange.first + 3)
            builder.addStyle(style = SpanStyle(color = Color.Transparent), start = matchRange.last - 3, end = matchRange.last + 1)
        }

        return TransformedText(text = builder.toAnnotatedString(), offsetMapping = OffsetMapping.Identity)
    }
}