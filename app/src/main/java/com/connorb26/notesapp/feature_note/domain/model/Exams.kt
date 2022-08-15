package com.connorb26.notesapp.feature_note.domain.model

class Exams(
) {
    private lateinit var exams: Map<String, String>

    fun addExam(name: String, date: String) {
        exams = exams + Pair(name, date)
    }

    fun removeExam(name: String) {
        exams = exams - name
    }
}