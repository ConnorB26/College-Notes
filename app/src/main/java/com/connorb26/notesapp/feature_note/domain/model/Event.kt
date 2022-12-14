package com.connorb26.notesapp.feature_note.domain.model

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val dtstart: Long,
    val dtend: Long,
    val allDay: Boolean,
    val eventLocation: String
)