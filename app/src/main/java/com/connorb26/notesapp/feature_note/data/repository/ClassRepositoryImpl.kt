package com.connorb26.notesapp.feature_note.data.repository

import com.connorb26.notesapp.feature_note.data.data_source.ClassDao
import com.connorb26.notesapp.feature_note.data.data_source.NoteDao
import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.repository.ClassRepository
import com.connorb26.notesapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class ClassRepositoryImpl(
    private val dao: ClassDao
): ClassRepository {
    override fun getClasses(): Flow<List<Class>> {
        return dao.getClasses()
    }

    override suspend fun getClassById(id: Int): Class? {
        return dao.getClassById(id)
    }

    override suspend fun insertClass(classObj: Class) {
        dao.insertClass(classObj)
    }

    override suspend fun deleteClass(classObj: Class) {
        dao.deleteClass(classObj)
    }
}