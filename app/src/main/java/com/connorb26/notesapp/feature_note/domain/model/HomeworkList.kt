package com.connorb26.notesapp.feature_note.domain.model

class HomeworkList(
) {
    private lateinit var homework: List<Homework>

    fun addHomework(hw: Homework) {
        homework = homework + hw
    }

    fun removeHomework(hw: Homework) {
        homework = homework - hw
    }
}