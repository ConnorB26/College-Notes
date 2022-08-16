package com.connorb26.notesapp.feature_note.domain.model

data class DateHolder (
    var day: String = "",
    var month: String = "",
    var year: String = "",
    var default: String = "Date"
) {
    override fun toString(): String {
        return if(day.isBlank() && month.isBlank() && year.isBlank()) {
            default
        } else {
            "$month/$day/$year"
        }
    }
}
