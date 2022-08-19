package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.*
import com.connorb26.notesapp.feature_note.domain.use_case.calendar.CalendarUseCases
import com.connorb26.notesapp.feature_note.domain.use_case.classes.ClassUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class AddEditClassViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases,
    private val classUseCases: ClassUseCases,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
): ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _classState = mutableStateOf(AddEditClassState())
    val classState: State<AddEditClassState> = _classState

    private val _firstDay = mutableStateOf(classState.value.firstDay)
    val firstDay: State<DateHolder> = _firstDay

    private val _lastDay = mutableStateOf(classState.value.lastDay)
    val lastDay: State<DateHolder> = _lastDay

    private var _classTimes = mutableStateOf(classState.value.classTimes)
    var classTimes: State<List<ClassTime>> = _classTimes

    private var _exams = mutableStateOf(classState.value.exams)
    var exams: State<List<Exam>> = _exams

    private var examsToDelete: MutableList<Exam> = mutableListOf()
    private var classTimesToDelete: MutableList<ClassTime> = mutableListOf()

    private var currentClassName: String? = null

    init {
        savedStateHandle.get<String>("className")?.let { className ->
            if(className.isNotEmpty()) {
                viewModelScope.launch {
                    classUseCases.getClass(className)?.also { classObj ->
                        currentClassName = classObj.name
                        _classState.value = classState.value.copy(
                            name = classObj.name,
                            classTimes = classObj.classTimes.classTimes,
                            exams = classObj.exams.exams,
                            homeworkList = classObj.homework.homework,
                            firstDay = classObj.firstDay,
                            lastDay = classObj.lastDay,
                            color = classObj.color
                        )
                        _classTimes.value = classObj.classTimes.classTimes
                        _exams.value = classObj.exams.exams
                        _firstDay.value = classObj.firstDay
                        _lastDay.value = classObj.lastDay
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
            is AddEditClassEvent.EnteredColor -> {
                _classState.value = classState.value.copy(
                    color = event.value
                )
            }
            is AddEditClassEvent.EnteredFirstDay -> {
                _firstDay.value = event.value
            }
            is AddEditClassEvent.EnteredLastDay -> {
                _lastDay.value = event.value
            }
            is AddEditClassEvent.EnteredClassDay -> {
                classTimes.value[classTimes.value.indexOf(event.classTime)].dayOfWeek = event.value
            }
            is AddEditClassEvent.EnteredClassLocation -> {
                classTimes.value[classTimes.value.indexOf(event.classTime)].location = event.value
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
                    val toRemove: ClassTime = _classTimes.value.lastOrNull()!!
                    classTimesToDelete += toRemove
                    _classTimes.value -= toRemove
                }
            }
            is AddEditClassEvent.AddExam -> {
                _exams.value += Exam()
            }
            is AddEditClassEvent.RemoveExam -> {
                if(_exams.value.isNotEmpty()) {
                    val toRemove: Exam = _exams.value.lastOrNull()!!
                    examsToDelete += toRemove
                    _exams.value -= toRemove
                }
            }
            is AddEditClassEvent.SaveClass -> {
                viewModelScope.launch {
                    try {
                        classUseCases.addClass(
                            Class(
                                name = classState.value.name,
                                classTimes = ClassTimes(classTimes.value),
                                exams = Exams(exams.value),
                                homework = HomeworkList(classState.value.homeworkList),
                                firstDay = firstDay.value,
                                lastDay = lastDay.value,
                                color = classState.value.color
                            )
                        )
                        for(exam: Exam in exams.value) {
                            if(exam.isValid()) {
                                if(exam.eventID != -1L) {
                                    calendarUseCases.deleteEvent(context, exam.eventID)
                                }
                                exam.eventID = calendarUseCases.addExam(
                                    context,
                                    exam.name,
                                    exam.date!!,
                                    exam.time!!,
                                    classState.value.name
                                )
                            }
                        }
                        for(classTime: ClassTime in classTimes.value) {
                            if(classTime.isValid()) {
                                if(classTime.eventID != -1L) {
                                    calendarUseCases.deleteEvent(context, classTime.eventID)
                                }
                                classTime.eventID = calendarUseCases.addClassTime(
                                    context,
                                    classState.value.name,
                                    classTime.dayOfWeek,
                                    classTime.startTime!!,
                                    classTime.endTime!!,
                                    firstDay.value,
                                    lastDay.value,
                                    classTime.location ?: ""
                                )
                            }
                        }
                        for(exam: Exam in examsToDelete) {
                            if(exam.eventID != -1L && exam.isValid()) {
                                calendarUseCases.deleteEvent(context, exam.eventID)
                            }
                        }
                        for(classTime: ClassTime in classTimesToDelete) {
                            if(classTime.eventID != -1L && classTime.isValid()) {
                                calendarUseCases.deleteEvent(context, classTime.eventID)
                            }
                        }
                        _eventFlow.emit(UiEvent.NavigateUp)
                    } catch(e: InvalidClassException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save class"
                            )
                        )
                    }
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