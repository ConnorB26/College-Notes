package com.connorb26.notesapp.feature_note.domain.use_case.calendar

data class CalendarUseCases(
    val getEventsRange: GetEventsRange,
    val getEventsDay: GetEventsDay,
    val addEvent: AddEvent,
    val addHW: AddHW,
    val editEvent: EditEvent,
    val deleteEvent: DeleteEvent,
    val getEventByID: GetEventByID,
    val addExam: AddExam,
    val updateExam: UpdateExam,
    val addClassTime: AddClassTime
)
