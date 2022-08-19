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
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder


class AddExam {
    operator fun invoke(context: Context, name: String, date: DateHolder, time: TimeHolder, className: String): Long {
        val timeInMillis: Long = Calendar.getInstance().run {
            set(("20"+date.year).toInt(), date.month.toInt()-1, date.day.toInt(), time.hour.toInt(), time.minute.toInt())
            timeInMillis
        }

        val timeZone: TimeZone = TimeZone.getDefault()
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, timeInMillis)
            put(CalendarContract.Events.DTEND, timeInMillis+7200000)
            put(CalendarContract.Events.TITLE, "$className $name")
            put(CalendarContract.Events.CALENDAR_ID, getCalendarId(context))
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
        }
        val uri: Uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!

        return uri.lastPathSegment!!.toLong()
    }

    private fun getCalendarId(context: Context): Long {
        var calId: Long = 0
        val calendars: Uri = CalendarContract.Calendars.CONTENT_URI
        val managedCursor: Cursor? = context.contentResolver
            .query(
                calendars, arrayOf("_id", "name"), null,
                null, null
            )
        if (managedCursor!!.moveToFirst()) {
            calId = managedCursor.getLong(
                managedCursor
                    .getColumnIndex("_id")
            )
        }
        managedCursor.close()
        return calId
    }
}