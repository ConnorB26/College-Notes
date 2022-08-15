package com.connorb26.notesapp.feature_note.presentation.add_edit_class

sealed class AddEditClassEvent {
    data class EnteredName(val value: String): AddEditClassEvent()
    data class EnteredLocation(val value: String): AddEditClassEvent()
    object SaveClass : AddEditClassEvent()
    object CancelClass : AddEditClassEvent()
}