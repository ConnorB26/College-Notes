package com.connorb26.notesapp.feature_note.domain.model

class ClassTimes(
) {
    private lateinit var classTimes: Map<String, Pair<Long, Long>>

    fun addClassTime(dayOfWeek: String, startTime: Long, endTime: Long) {
        classTimes = classTimes + Pair(dayOfWeek, Pair(startTime, endTime))
    }

    fun clearDay(dayOfWeek: String) {
        classTimes = classTimes - dayOfWeek
    }
}