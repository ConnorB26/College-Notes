package com.connorb26.notesapp.feature_note.presentation.classes

import com.connorb26.notesapp.feature_note.domain.model.Class

data class ClassesState(
    val classes: List<Class> = emptyList()
)
