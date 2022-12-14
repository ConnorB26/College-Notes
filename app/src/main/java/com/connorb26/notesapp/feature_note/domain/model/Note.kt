package com.connorb26.notesapp.feature_note.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.connorb26.notesapp.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(White, LightGray, Maroon, RedOrange, LightGreen, Green, BabyBlue, Blue, Violet)
    }
}

class InvalidNoteException(message: String): Exception(message)