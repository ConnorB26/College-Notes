package com.connorb26.notesapp.feature_note.domain.model

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val dtstart: Long,
    val dtend: Long,
    val allDay: Boolean,
    val eventLocation: String,
    val type: EventType = descriptionToEvent(description)
)

enum class EventType {
    EVENT,
    HOMEWORK,
    CLASS
}

private fun descriptionToEvent(description: String): EventType {
    return if(description.contains("HW")) {
        EventType.HOMEWORK
    }
    else if(description.contains("Class")) {
        EventType.CLASS
    }
    else {
        EventType.EVENT
    }
}