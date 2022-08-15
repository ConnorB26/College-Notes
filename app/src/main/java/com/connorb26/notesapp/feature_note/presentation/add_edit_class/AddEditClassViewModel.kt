package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.use_case.classes.ClassUseCases
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.NoteTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditClassViewModel @Inject constructor(
    private val classUseCases: ClassUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentClassName: String? = null

    private val _classNameField = mutableStateOf(ClassTextFieldState())
    val classNameField: State<ClassTextFieldState> = _classNameField

    private val _location = mutableStateOf(ClassTextFieldState())
    val location: State<ClassTextFieldState> = _location

    init {
        savedStateHandle.get<String>("className")?.let { className ->
            if(className.isNotEmpty()) {
                viewModelScope.launch {
                    classUseCases.getClass(className)?.also { classObj ->
                        currentClassName = classObj.name
                        _classNameField.value = classNameField.value.copy(
                            text = classObj.name
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditClassEvent) {
        when(event) {
            is AddEditClassEvent.EnteredName -> {
                _classNameField.value = classNameField.value.copy(
                    text = event.value
                )
            }

            is AddEditClassEvent.EnteredLocation -> {
                _location.value = location.value.copy(
                    text = event.value
                )
            }

            is AddEditClassEvent.SaveClass -> {
                viewModelScope.launch {
                    /*classUseCases.addClass(
                        Class(

                        )
                    )*/
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
            is AddEditClassEvent.CancelClass -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object NavigateUp: UiEvent()
    }
}