package com.connorb26.notesapp.feature_note.domain.model

data class ClassTime(
    var dayOfWeek: String = "",
    var location: String = "",
    var startTime: TimeHolder? = null,
    var endTime: TimeHolder? = null,
    var eventID: Long = -1
) {
    fun isValid(): Boolean {
        return (dayOfWeek.isNotBlank() && startTime != null && endTime != null)
    }
}

