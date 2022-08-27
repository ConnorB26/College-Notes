package com.connorb26.notesapp.feature_note.domain.use_case.calendar

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


class AddExam {
    operator fun invoke(context: Context, exam: Exam, className: String, calID: Long): Long {
        val name = exam.name
        val date = exam.date!!
        val time = exam.time!!

        val timeInMillis: Long = Calendar.getInstance().run {
            set(date.year, date.month, date.day, time.hour, time.minute)
            timeInMillis
        }

        val timeZone: TimeZone = TimeZone.getDefault()
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, timeInMillis)
            put(CalendarContract.Events.DTEND, timeInMillis+7200000)
            put(CalendarContract.Events.TITLE, "$className $name")
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
        }
        val uri: Uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!

        return uri.lastPathSegment!!.toLong()
    }
}