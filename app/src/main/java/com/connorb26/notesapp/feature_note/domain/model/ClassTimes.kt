package com.connorb26.notesapp.feature_note.domain.model

class ClassTimes(
    var classTimes: List<ClassTime> = emptyList()
) {
    fun addClassTime(classTime: ClassTime) {
        classTimes = classTimes + classTime
    }

    fun removeClassTime(classTime: ClassTime) {
        classTimes = classTimes - classTime
    }
}