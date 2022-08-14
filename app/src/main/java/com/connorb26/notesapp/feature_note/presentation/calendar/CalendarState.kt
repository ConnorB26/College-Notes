package com.connorb26.notesapp.feature_note.presentation.calendar

import com.connorb26.notesapp.feature_note.domain.model.Event

data class CalendarState(
    val events: List<Event> = emptyList()
)
