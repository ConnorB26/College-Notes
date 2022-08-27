package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.connorb26.notesapp.feature_note.domain.model.ClassTime
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.Exam

data class AddEditClassState(
    val name: String = "",
    val color: Int = Color.Black.toArgb(),
    var firstDay: DateHolder = DateHolder(default = "First Day"),
    var lastDay: DateHolder = DateHolder(default = "Last Day"),
    var classTimes: List<ClassTime> = emptyList(),
    val exams: List<Exam> = emptyList()
)