package com.connorb26.notesapp.feature_note.presentation.classes

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.model.ClassTime
import com.connorb26.notesapp.feature_note.domain.model.Exam
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.use_case.calendar.CalendarUseCases
import com.connorb26.notesapp.feature_note.domain.use_case.classes.ClassUseCases
import com.connorb26.notesapp.feature_note.domain.util.NoteOrder
import com.connorb26.notesapp.feature_note.domain.util.OrderType
import com.connorb26.notesapp.feature_note.presentation.notes.NotesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ClassesViewModel @Inject constructor(
    private val classUseCases: ClassUseCases,
    private val calendarUseCases: CalendarUseCases,
    @ApplicationContext val context: Context
): ViewModel() {
    private val _state = mutableStateOf(ClassesState())
    val state: State<ClassesState> = _state

    private val _deleteDialog = mutableStateOf(false)
    val deleteDialog: State<Boolean> = _deleteDialog
    var deletingClass: Class? = null

    private var getClassesJob: Job? = null

    init {
        getClasses()
    }

    fun onEvent(event: ClassesEvent) {
        when(event) {
            is ClassesEvent.DeleteClass -> {
                viewModelScope.launch {
                    for(exam: Exam in event.classObj.exams.exams) {
                        calendarUseCases.deleteEvent(context, exam.eventID)
                    }
                    for(classTime: ClassTime in event.classObj.classTimes.classTimes) {
                        calendarUseCases.deleteEvent(context, classTime.eventID)
                    }
                    classUseCases.deleteClass(event.classObj)
                }
            }
            is ClassesEvent.DeleteDialogEnable -> {
                _deleteDialog.value = true
                deletingClass = event.classObj
            }
            is ClassesEvent.DeleteDialogDisable -> {
                _deleteDialog.value = false
                deletingClass = null
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