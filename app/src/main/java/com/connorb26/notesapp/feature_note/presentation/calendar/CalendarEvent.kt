package com.connorb26.notesapp.feature_note.presentation.calendar

import android.database.Cursor

sealed class CalendarEvent {
    data class UpdateDayEvents(val cursor: Cursor?): CalendarEvent()
    object AddEvent: CalendarEvent()
}