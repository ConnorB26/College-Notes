package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.connorb26.notesapp.feature_note.domain.model.ClassTime
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import java.time.*
import java.time.temporal.ChronoUnit


class UpdateClassTime {
    operator fun invoke(context: Context, classTime: ClassTime, className: String, firstDay: DateHolder, lastDay: DateHolder) {
        val dayOfWeek = classTime.dayOfWeek
        val location = classTime.location
        val startTime = classTime.startTime!!
        val endTime = classTime.endTime!!
        val eventId = classTime.eventID

        val startTimeInMillis: Long = Calendar.getInstance().run {
            set(firstDay.year, firstDay.month, firstDay.day, startTime.hour, startTime.minute)
            timeInMillis
        }
        val endTimeInMillis: Long = Calendar.getInstance().run {
            set(firstDay.year, firstDay.month, firstDay.day, endTime.hour, endTime.minute)
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTimeInMillis)
            put(CalendarContract.Events.DTEND, endTimeInMillis)
            put(CalendarContract.Events.RRULE, getRRule(dayOfWeek, lastDay))
            put(CalendarContract.Events.TITLE, className)
            put(CalendarContract.Events.EVENT_LOCATION, location)
        }
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        context.contentResolver.update(uri, values, null, null)

        Log.d("RRULE", getRRule(dayOfWeek, lastDay))
    }

    private fun getRRule(dayOfWeek: String, lastDay: DateHolder): String {
        return "FREQ=WEEKLY;" +
                "BYDAY=${getShortenedDay(dayOfWeek)};" +
                "UNTIL=${lastDay.year}${if(lastDay.month+1 < 10) "0${lastDay.month+1}" else lastDay.month+1}${lastDay.day+1}T000000Z;" +
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
}