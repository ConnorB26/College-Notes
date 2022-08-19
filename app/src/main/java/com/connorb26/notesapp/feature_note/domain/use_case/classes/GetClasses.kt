package com.connorb26.notesapp.feature_note.domain.use_case.classes

import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.repository.ClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetClasses(
    private val repository: ClassRepository
) {
    operator fun invoke(): Flow<List<Class>> {
        return repository.getClasses().map { classes ->
            classes.sortedBy { it.name }
        }
    }
}