package com.connorb26.notesapp.feature_note.presentation.add_edit_note

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    data class EnabledDisableColorPicker(val value: Boolean): AddEditNoteEvent()
    object ToggleColorSection: AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
    object Navigate: AddEditNoteEvent()
}