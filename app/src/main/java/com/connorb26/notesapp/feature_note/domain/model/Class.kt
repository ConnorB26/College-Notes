package com.connorb26.notesapp.feature_note.domain.model

import android.icu.util.Calendar
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Class(
    @PrimaryKey val name: String,
    val location: String,
    val classTimes: ClassTimes,
    val exams: Exams,
    val homework: HomeworkList
)