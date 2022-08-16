package com.connorb26.notesapp.feature_note.domain.model

class Exams(
    var exams: List<Exam> = emptyList()
) {
    fun addExam(exam: Exam) {
        exams = exams + exam
    }

    fun removeExam(exam: Exam) {
        exams = exams - exam
    }
}