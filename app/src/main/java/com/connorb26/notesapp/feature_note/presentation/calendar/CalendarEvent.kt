package com.connorb26.notesapp.feature_note.presentation.calendar

import android.database.Cursor
import com.connorb26.notesapp.feature_note.domain.model.Class

sealed class CalendarEvent {
    data class UpdateDayEvents(val year: Int, val month: Int, val day: Int): CalendarEvent()
    data class AddEvent(val startTime: Long): CalendarEvent()
    data class AddHomework(val startTime: Long, val classObj: Class): CalendarEvent()
    data class EditEvent(val id: Long): CalendarEvent()
}