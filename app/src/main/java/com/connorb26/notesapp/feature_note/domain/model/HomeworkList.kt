package com.connorb26.notesapp.feature_note.domain.model

class HomeworkList(
    var homework: List<Homework> = emptyList()
) {
    fun addHomework(hw: Homework) {
        homework = homework + hw
    }

    fun removeHomework(hw: Homework) {
        homework = homework - hw
    }
}