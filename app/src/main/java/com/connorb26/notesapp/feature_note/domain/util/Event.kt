package com.connorb26.notesapp.feature_note.domain.util

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val dtstart: Long,
    val dtend: Long,
    val allDay: Boolean,
    val eventLocation: String
) {

}