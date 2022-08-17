package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder

class UpdateExam {
    operator fun invoke(context: Context, id: Long, name: String, date: DateHolder, time: TimeHolder, className: String) {
        val timeInMillis: Long = Calendar.getInstance().run {
            set(("20"+date.year).toInt(), date.month.toInt()-1, date.day.toInt(), time.hour.toInt(), time.minute.toInt())
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, timeInMillis)
            put(CalendarContract.Events.DTEND, timeInMillis)
            put(CalendarContract.Events.TITLE, "$className $name")
        }
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id)
        context.contentResolver.update(uri, values, null, null)
    }
}