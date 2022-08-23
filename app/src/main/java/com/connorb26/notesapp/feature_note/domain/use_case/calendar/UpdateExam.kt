package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.Exam
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder


class UpdateExam {
    operator fun invoke(context: Context, exam: Exam, className: String) {
        val name = exam.name
        val date = exam.date!!
        val time = exam.time!!
        val eventId = exam.eventID

        val timeInMillis: Long = Calendar.getInstance().run {
            set(date.year, date.month, date.day, time.hour, time.minute)
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, timeInMillis)
            put(CalendarContract.Events.DTEND, timeInMillis+7200000)
            put(CalendarContract.Events.TITLE, "$className $name")
        }
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        context.contentResolver.update(uri, values, null, null)
    }
}