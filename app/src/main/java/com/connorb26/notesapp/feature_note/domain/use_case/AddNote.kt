package com.connorb26.notesapp.feature_note.domain.use_case

import com.connorb26.notesapp.feature_note.domain.model.InvalidNoteException
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if(note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty.")
        }
        if(note.content.isBlank()) {
            throw InvalidNoteException("The body of the note can't be empty.")
        }

        repository.insertNote(note)
    }
}