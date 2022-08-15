package com.connorb26.notesapp.feature_note.presentation.classes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.use_case.classes.ClassUseCases
import com.connorb26.notesapp.feature_note.domain.util.NoteOrder
import com.connorb26.notesapp.feature_note.domain.util.OrderType
import com.connorb26.notesapp.feature_note.presentation.notes.NotesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val classUseCases: ClassUseCases
): ViewModel() {
    private val _state = mutableStateOf(ClassesState())
    val state: State<ClassesState> = _state

    private var recentlyDeletedClass: Class? = null

    private var getClassesJob: Job? = null

    init {
        getClasses()
    }

    fun onEvent(event: ClassesEvent) {
        when(event) {
            is ClassesEvent.DeleteClass -> {
                viewModelScope.launch {
                    classUseCases.deleteClass(event.classObj)
                    recentlyDeletedClass = event.classObj
                }
            }
            is ClassesEvent.RestoreClass -> {
                viewModelScope.launch {
                    classUseCases.addClass(recentlyDeletedClass ?: return@launch)
                    recentlyDeletedClass = null
                }
            }
        }
    }

    private fun getClasses() {
        getClassesJob?.cancel()
        getClassesJob = classUseCases.getClasses()
            .onEach { classes ->
                _state.value = state.value.copy(
                    classes = classes
                )
            }
            .launchIn(viewModelScope)
    }
}