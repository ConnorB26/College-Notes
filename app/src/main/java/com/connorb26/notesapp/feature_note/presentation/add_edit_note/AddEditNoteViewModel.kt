package com.connorb26.notesapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.InvalidNoteException
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.use_case.notes.NoteUseCases
import com.connorb26.notesapp.feature_note.presentation.util.VariableColor
import com.connorb26.notesapp.ui.theme.DarkGray
import com.connorb26.notesapp.ui.theme.White
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter a title"
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteState = mutableStateOf(AddEditNoteState())
    val noteState: State<AddEditNoteState> = _noteState

    private val _noteColor = mutableStateOf(White.toArgb())
    val noteColor: State<Int> = _noteColor

    private val _complementColor = mutableStateOf(DarkGray.toArgb())
    val complementColor: State<Int> = _complementColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content
                        )
                        _noteColor.value = note.color
                        _complementColor.value = VariableColor.getColor(Color(note.color)).toArgb()
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
                _complementColor.value = VariableColor.getColor(Color(event.color)).toArgb()
            }
            is AddEditNoteEvent.ToggleColorSection -> {
                _noteState.value = noteState.value.copy(
                    colorSectionVisible = !noteState.value.colorSectionVisible
                )
            }
            is AddEditNoteEvent.EnabledDisableColorPicker -> {
                _noteState.value = noteState.value.copy(
                    colorPickerVisible = event.value
                )
            }
            is AddEditNoteEvent.Navigate -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object NavigateUp: UiEvent()
    }
}