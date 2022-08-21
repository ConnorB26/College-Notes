package com.connorb26.notesapp.feature_note.data.data_source

import androidx.room.*
import com.connorb26.notesapp.feature_note.domain.model.Class
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassDao {
    @Query("SELECT * FROM class")
    fun getClasses(): Flow<List<Class>>

    @Query("SELECT * FROM class WHERE id = :id")
    suspend fun getClassById(id: Int): Class?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classObj: Class)

    @Delete
    suspend fun deleteClass(classObj: Class)
}