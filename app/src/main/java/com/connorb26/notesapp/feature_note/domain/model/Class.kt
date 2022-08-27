package com.connorb26.notesapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Class(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val classTimes: ClassTimes,
    val exams: Exams,
    val firstDay: DateHolder,
    val lastDay: DateHolder,
    val color: Int
)

class InvalidClassException(message: String): Exception(message)