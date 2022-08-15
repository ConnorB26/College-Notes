package com.connorb26.notesapp.feature_note.domain.model

data class Homework(
    val id: Long,
    val title: String,
    val description: String,
    val dueDate: Long,
    val recurringFrequency: String,
    val recurringEndDate: Long
)