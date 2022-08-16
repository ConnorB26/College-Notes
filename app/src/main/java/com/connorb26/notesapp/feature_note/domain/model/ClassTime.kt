package com.connorb26.notesapp.feature_note.domain.model

data class ClassTime(
    var dayOfWeek: String = "",
    var startTime: TimeHolder? = null,
    var endTime: TimeHolder? = null
)
