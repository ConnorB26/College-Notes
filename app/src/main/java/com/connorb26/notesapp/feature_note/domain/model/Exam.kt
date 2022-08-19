package com.connorb26.notesapp.feature_note.domain.model

data class Exam(
    var name: String = "",
    var date: DateHolder? = null,
    var time: TimeHolder? = null,
    var eventID: Long = -1
) {
    fun isValid(): Boolean {
        return (name.isNotBlank() && date != null && time != null)
    }
}
