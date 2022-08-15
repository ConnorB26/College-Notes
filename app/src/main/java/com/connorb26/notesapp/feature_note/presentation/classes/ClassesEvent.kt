package com.connorb26.notesapp.feature_note.presentation.classes

import com.connorb26.notesapp.feature_note.domain.model.Class

sealed class ClassesEvent {
    data class DeleteClass(val classObj: Class): ClassesEvent()
    object RestoreClass: ClassesEvent()
}
