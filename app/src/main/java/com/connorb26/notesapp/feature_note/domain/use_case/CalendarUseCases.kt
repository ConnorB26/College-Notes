package com.connorb26.notesapp.feature_note.domain.use_case

data class CalendarUseCases(
    val getEventsRange: GetEventsRange,
    val getEventsDay: GetEventsDay,
    val addEvent: AddEvent,
    val addHW: AddHW,
    val editEvent: EditEvent
)
