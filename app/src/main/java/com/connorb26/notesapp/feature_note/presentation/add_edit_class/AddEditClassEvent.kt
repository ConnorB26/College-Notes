package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import com.connorb26.notesapp.feature_note.domain.model.ClassTime
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.Exam
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder

sealed class AddEditClassEvent {
    data class EnteredName(val value: String): AddEditClassEvent()
    data class EnteredLocation(val value: String): AddEditClassEvent()
    data class EnteredClassDay(val classTime: ClassTime, val value: String): AddEditClassEvent()
    data class EnteredClassStartTime(val classTime: ClassTime, val value: TimeHolder): AddEditClassEvent()
    data class EnteredClassEndTime(val classTime: ClassTime, val value: TimeHolder): AddEditClassEvent()
    data class EnteredExamName(val exam: Exam, val value: String): AddEditClassEvent()
    data class EnteredExamDate(val exam: Exam, val value: DateHolder): AddEditClassEvent()
    data class EnteredExamTime(val exam: Exam, val value: TimeHolder): AddEditClassEvent()
    object AddClassTime: AddEditClassEvent()
    object RemoveClassTime: AddEditClassEvent()
    object AddExam: AddEditClassEvent()
    object RemoveExam: AddEditClassEvent()
    object SaveClass: AddEditClassEvent()
    object CancelClass: AddEditClassEvent()
}