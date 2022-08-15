package com.connorb26.notesapp.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.util.Converters

@Database(
    entities = [Class::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ClassDatabase: RoomDatabase() {
    abstract val classDao: ClassDao

    companion object {
        const val DATABASE_NAME = "classes_db"
    }
}