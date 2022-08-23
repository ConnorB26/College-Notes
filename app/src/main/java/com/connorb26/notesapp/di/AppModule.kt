package com.connorb26.notesapp.di

import android.app.Application
import androidx.room.Room
import com.connorb26.notesapp.feature_note.data.data_source.ClassDatabase
import com.connorb26.notesapp.feature_note.data.data_source.NoteDatabase
import com.connorb26.notesapp.feature_note.data.repository.ClassRepositoryImpl
import com.connorb26.notesapp.feature_note.data.repository.NoteRepositoryImpl
import com.connorb26.notesapp.feature_note.domain.repository.ClassRepository
import com.connorb26.notesapp.feature_note.domain.repository.NoteRepository
import com.connorb26.notesapp.feature_note.domain.use_case.calendar.*
import com.connorb26.notesapp.feature_note.domain.use_case.classes.*
import com.connorb26.notesapp.feature_note.domain.use_case.notes.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideClassDatabase(app: Application): ClassDatabase {
        return Room.databaseBuilder(
            app,
            ClassDatabase::class.java,
            ClassDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideClassRepository(db: ClassDatabase): ClassRepository {
        return ClassRepositoryImpl(db.classDao)
    }

    @Provides
    @Singleton
    fun provideClassUseCases(repository: ClassRepository): ClassUseCases {
        return ClassUseCases(
            getClasses = GetClasses(repository),
            deleteClass = DeleteClass(repository),
            addClass = AddClass(repository),
            getClass = GetClass(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCalendarUseCases(): CalendarUseCases {
        return CalendarUseCases(
            getEventsDay = GetEventsDay(),
            getEventsRange = GetEventsRange(),
            addEvent = AddEvent(),
            addHW = AddHW(),
            editEvent = EditEvent(),
            deleteEvent = DeleteEvent(),
            addExam = AddExam(),
            getEventByID = GetEventByID(),
            addClassTime = AddClassTime(),
            updateExam = UpdateExam(),
            updateClassTime = UpdateClassTime()
        )
    }
}