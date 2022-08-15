package com.connorb26.notesapp.feature_note.domain.util

import androidx.room.TypeConverter
import com.connorb26.notesapp.feature_note.domain.model.ClassTimes
import com.connorb26.notesapp.feature_note.domain.model.Exams
import com.connorb26.notesapp.feature_note.domain.model.HomeworkList
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun storedStringToExams(value: String): Exams {
        return Gson().fromJson(value, Exams::class.java)
    }

    @TypeConverter
    fun examsToStoredString(exams: Exams): String {
        return Gson().toJson(exams)
    }

    @TypeConverter
    fun storedStringToClassTimes(value: String): ClassTimes {
        return Gson().fromJson(value, ClassTimes::class.java)
    }

    @TypeConverter
    fun classTimesToStoredString(classTimes: ClassTimes): String {
        return Gson().toJson(classTimes)
    }

    @TypeConverter
    fun storedStringToHomeworkList(value: String): HomeworkList {
        return Gson().fromJson(value, HomeworkList::class.java)
    }

    @TypeConverter
    fun homeworkListToStoredString(homeworkList: HomeworkList): String {
        return Gson().toJson(homeworkList)
    }
}