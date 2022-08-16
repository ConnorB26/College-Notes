package com.connorb26.notesapp.feature_note.domain.model

data class Exam(
    var name: String = "",
    var date: DateHolder = DateHolder(),
    var time: TimeHolder = TimeHolder()
)