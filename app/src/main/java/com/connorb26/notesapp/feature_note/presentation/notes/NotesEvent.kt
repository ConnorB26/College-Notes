package com.connorb26.notesapp.feature_note.presentation.notes

import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class DeleteDialogEnable(val note: Note): NotesEvent()
    object DeleteDialogDisable : NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
}
