package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.*
import com.connorb26.notesapp.feature_note.domain.use_case.classes.ClassUseCases
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

    private val _classState = mutableStateOf(AddEditClassState())
    val classState: State<AddEditClassState> = _classState

    private var _classTimes = mutableStateOf(classState.value.classTimes)
    var classTimes: State<List<ClassTime>> = _classTimes

    private var _exams = mutableStateOf(classState.value.exams)
    var exams: State<List<Exam>> = _exams

    private var currentClassName: String? = null

    init {
        savedStateHandle.get<String>("className")?.let { className ->
            if(className.isNotEmpty()) {
                viewModelScope.launch {
                    classUseCases.getClass(className)?.also { classObj ->
                        currentClassName = classObj.name
                        _classState.value = classState.value.copy(
                            name = classObj.name,
                            location = classObj.location,
                            classTimes = classObj.classTimes.classTimes,
                            exams = classObj.exams.exams,
                            homeworkList = classObj.homework.homework
                        )
                        _classTimes.value = classObj.classTimes.classTimes
                        _exams.value = classObj.exams.exams
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditClassEvent) {
        when(event) {
            is AddEditClassEvent.EnteredName -> {
                _classState.value = classState.value.copy(
                    name = event.value
                )
            }
            is AddEditClassEvent.EnteredLocation -> {
                _classState.value = classState.value.copy(
                    location = event.value
                )
            }
            is AddEditClassEvent.EnteredClassDay -> {
                classTimes.value[classTimes.value.indexOf(event.classTime)].dayOfWeek = event.value
            }
            is AddEditClassEvent.EnteredClassStartTime -> {
                classTimes.value[classTimes.value.indexOf(event.classTime)].startTime = event.value
            }
            is AddEditClassEvent.EnteredClassEndTime -> {
                classTimes.value[classTimes.value.indexOf(event.classTime)].endTime = event.value
            }
            is AddEditClassEvent.EnteredExamName -> {
                exams.value[exams.value.indexOf(event.exam)].name = event.value
            }
            is AddEditClassEvent.EnteredExamDate -> {
                exams.value[exams.value.indexOf(event.exam)].date = event.value
            }
            is AddEditClassEvent.EnteredExamTime -> {
                exams.value[exams.value.indexOf(event.exam)].time = event.value
            }
            is AddEditClassEvent.AddClassTime -> {
                _classTimes.value += ClassTime()
            }
            is AddEditClassEvent.RemoveClassTime -> {
                if(_classTimes.value.isNotEmpty()) {
                    _classTimes.value -= _classTimes.value.lastOrNull()!!
                }
            }
            is AddEditClassEvent.AddExam -> {
                _exams.value += Exam()
            }
            is AddEditClassEvent.RemoveExam -> {
                if(_exams.value.isNotEmpty()) {
                    _exams.value -= _exams.value.lastOrNull()!!
                }
            }
            is AddEditClassEvent.SaveClass -> {
                viewModelScope.launch {
                    classUseCases.addClass(
                        Class(
                            name = classState.value.name,
                            location = classState.value.location,
                            classTimes = ClassTimes(classTimes.value),
                            exams = Exams(exams.value),
                            homework = HomeworkList(classState.value.homeworkList)
                        )
                    )
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