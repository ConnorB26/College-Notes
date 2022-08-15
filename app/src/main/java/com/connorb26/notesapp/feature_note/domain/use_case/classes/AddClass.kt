package com.connorb26.notesapp.feature_note.domain.use_case.classes

import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.repository.ClassRepository

class AddClass (
    private val repository: ClassRepository
) {
    suspend operator fun invoke(classObj: Class) {
        repository.insertClass(classObj)
    }
}