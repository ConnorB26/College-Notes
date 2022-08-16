package com.connorb26.notesapp.feature_note.domain.model

data class TimeHolder (
    var hour: String = "",
    var minute: String = "",
    var ampm: String = "",
    var default: String = "Time"
) {
    override fun toString(): String {
        return if(hour.isBlank() && minute.isBlank() && ampm.isBlank()) {
            default
        } else {
            "$hour:$minute $ampm"
        }
    }
}