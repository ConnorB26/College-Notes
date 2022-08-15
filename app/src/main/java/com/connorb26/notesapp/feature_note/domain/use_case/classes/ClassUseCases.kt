package com.connorb26.notesapp.feature_note.domain.use_case.classes

import com.connorb26.notesapp.feature_note.domain.use_case.notes.AddNote
import com.connorb26.notesapp.feature_note.domain.use_case.notes.DeleteNote
import com.connorb26.notesapp.feature_note.domain.use_case.notes.GetNote
import com.connorb26.notesapp.feature_note.domain.use_case.notes.GetNotes

data class ClassUseCases(
    val getClasses: GetClasses,
    val deleteClass: DeleteClass,
    val addClass: AddClass,
    val getClass: GetClass
)
