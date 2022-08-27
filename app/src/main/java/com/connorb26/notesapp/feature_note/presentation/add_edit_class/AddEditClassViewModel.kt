package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
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
import com.connorb26.notesapp.feature_note.presentation.util.VariableColor
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

    private val _clampedColor = mutableStateOf(VariableColor.setLuminance(Color(classState.value.color), 0.4f))
    val clampedColor: State<Color> = _clampedColor

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
    private var currentClassId: Int? = null

    init {
        savedStateHandle.get<Int>("classId")?.let { classId ->
            if(classId != -1) {
                viewModelScope.launch {
                    classUseCases.getClass(classId)?.also { classObj ->
                        currentClassName = classObj.name
                        currentClassId = classObj.id
                        _classState.value = classState.value.copy(
                            name = classObj.name,
                            classTimes = classObj.classTimes.classTimes,
                            exams = classObj.exams.exams,
                            firstDay = classObj.firstDay,
                            lastDay = classObj.lastDay,
                            color = classObj.color
                        )
                        _classTimes.value = classObj.classTimes.classTimes
                        _exams.value = classObj.exams.exams
                        _firstDay.value = classObj.firstDay
                        _lastDay.value = classObj.lastDay
                        _clampedColor.value = VariableColor.setLuminance(Color(classObj.color), 0.4f)
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
                _clampedColor.value = VariableColor.setLuminance(Color(event.value), 0.4f)
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
                        if(!checkPermission()) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar(
                                    message = "This app needs permissions to read/write to the calendar"
                                )
                            )
                            return@launch
                        }

                        for(exam: Exam in exams.value) {
                            if(exam.isValid()) {
                                if(exam.eventID == -1L) {
                                    exam.eventID = calendarUseCases.addExam(
                                        context,
                                        exam,
                                        classState.value.name,
                                        event.calID
                                    )
                                }
                                else {
                                    calendarUseCases.updateExam(
                                        context,
                                        exam,
                                        classState.value.name
                                    )
                                }
                            }
                        }
                        for(classTime: ClassTime in classTimes.value) {
                            if(classTime.isValid()) {
                                if(classTime.eventID == -1L) {
                                    classTime.eventID = calendarUseCases.addClassTime(
                                        context,
                                        classTime,
                                        classState.value.name,
                                        firstDay.value,
                                        lastDay.value,
                                        event.calID
                                    )
                                }
                                else {
                                    calendarUseCases.updateClassTime(
                                        context,
                                        classTime,
                                        classState.value.name,
                                        firstDay.value,
                                        lastDay.value
                                    )
                                }
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

                        classUseCases.addClass(
                            Class(
                                name = classState.value.name,
                                classTimes = ClassTimes(classTimes.value),
                                exams = Exams(exams.value),
                                firstDay = firstDay.value,
                                lastDay = lastDay.value,
                                color = classState.value.color,
                                id = currentClassId
                            )
                        )

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
            is AddEditClassEvent.ShowSnackbarMessage -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = event.value
                        )
                    )
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        val permission1 = Manifest.permission.READ_CALENDAR
        val res1: Int = context.checkCallingOrSelfPermission(permission1)
        val permission2 = Manifest.permission.WRITE_CALENDAR
        val res2: Int = context.checkCallingOrSelfPermission(permission2)

        return res1 == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object NavigateUp: UiEvent()
    }
}