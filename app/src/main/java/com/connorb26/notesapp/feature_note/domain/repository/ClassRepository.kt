package com.connorb26.notesapp.feature_note.domain.repository

import com.connorb26.notesapp.feature_note.domain.model.Class
import kotlinx.coroutines.flow.Flow

interface ClassRepository {
    fun getClasses(): Flow<List<Class>>

    suspend fun getClassByName(name: String): Class?

    suspend fun insertClass(classObj: Class)

    suspend fun deleteClass(classObj: Class)
}