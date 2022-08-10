package com.connorb26.notesapp.feature_note.presentation.calendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connorb26.notesapp.feature_note.domain.use_case.NoteUseCases
import com.connorb26.notesapp.feature_note.presentation.notes.NotesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
): ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
}