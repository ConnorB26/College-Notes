package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import java.time.*
import java.time.temporal.ChronoUnit


class AddClassTime {
    operator fun invoke(context: Context, className: String, dayOfWeek: String, startTime: TimeHolder,
                        endTime: TimeHolder, firstDay: DateHolder, lastDay: DateHolder, location: String): Long {
        val startTimeInMillis: Long = Calendar.getInstance().run {
            set(("20"+firstDay.year).toInt(), firstDay.month.toInt()-1, firstDay.day.toInt(), startTime.hour, startTime.minute)
            timeInMillis
        }
        val endTimeInMillis: Long = Calendar.getInstance().run {
            set(("20"+firstDay.year).toInt(), firstDay.month.toInt()-1, firstDay.day.toInt(), endTime.hour, endTime.minute)
            timeInMillis
        }

        val timeZone: TimeZone = TimeZone.getDefault()
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTimeInMillis)
            put(CalendarContract.Events.DTEND, endTimeInMillis)
            put(CalendarContract.Events.RRULE, getRRule(dayOfWeek, lastDay))
            put(CalendarContract.Events.TITLE, className)
            put(CalendarContract.Events.EVENT_LOCATION, location)
            put(CalendarContract.Events.CALENDAR_ID, getCalendarId(context))
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)

        }
        val uri: Uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!

        return uri.lastPathSegment!!.toLong()
    }

    private fun getRRule(dayOfWeek: String, lastDay: DateHolder): String {
        return "FREQ=WEEKLY;" +
                "BYDAY=${getShortenedDay(dayOfWeek)};" +
                "UNTIL=20${lastDay.year}${if(lastDay.month.toInt() < 10) "0${lastDay.month}" else lastDay.month}${lastDay.day.toInt()+1}T000000Z;" +
                "WKST=${getShortenedDay("Sunday")}"
    }

    private fun getShortenedDay(dayOfWeek: String): String {
        return when(dayOfWeek) {
            "Sunday" -> "SU"
            "Monday" -> "MO"
            "Tuesday" -> "TU"
            "Wednesday" -> "WE"
            "Thursday" -> "TH"
            "Friday" -> "FR"
            "Saturday" -> "SA"
            else -> "MO"
        }
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