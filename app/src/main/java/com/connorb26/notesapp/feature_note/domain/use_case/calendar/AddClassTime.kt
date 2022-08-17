package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.provider.CalendarContract
import com.connorb26.notesapp.feature_note.domain.model.DateHolder
import com.connorb26.notesapp.feature_note.domain.model.TimeHolder
import java.time.DayOfWeek
import java.time.LocalDate

class AddClassTime {
    operator fun invoke(context: Context, className: String, dayOfWeek: String, startTime: TimeHolder, endTime: TimeHolder, firstDay: DateHolder, lastDay: DateHolder): Long {
        val nextWeekday: LocalDate = getNext(dayOfWeek, firstDay)
        val startTimeInMillis: Long = Calendar.getInstance().run {
            set(nextWeekday.year, nextWeekday.monthValue, nextWeekday.dayOfMonth, startTime.hour.toInt(), startTime.minute.toInt())
            timeInMillis
        }
        val endTimeInMillis: Long = Calendar.getInstance().run {
            set(nextWeekday.year, nextWeekday.monthValue, nextWeekday.dayOfMonth, endTime.hour.toInt(), endTime.minute.toInt())
            timeInMillis
        }

        val timeZone: TimeZone = TimeZone.getDefault()
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTimeInMillis)
            put(CalendarContract.Events.DTEND, endTimeInMillis)
            put(CalendarContract.Events.TITLE, className)
            put(CalendarContract.Events.CALENDAR_ID, getCalendarId(context))
            put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
            put(CalendarContract.Events.RRULE, getRRule(dayOfWeek, lastDay))
        }
        val uri: Uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)!!

        return uri.lastPathSegment!!.toLong()
    }

    private fun getRRule(dayOfWeek: String, lastDay: DateHolder): String {
        var result: String = "FREQ=WEEKLY;BYDAY="

        result += when(dayOfWeek) {
            "Sunday" -> "SU"
            "Monday" -> "MO"
            "Tuesday" -> "TU"
            "Wednesday" -> "WE"
            "Thursday" -> "TH"
            "Friday" -> "FR"
            "Saturday" -> "SA"
            else -> "MO"
        }

        result += ";UNTIL="

        result += Calendar.getInstance().run {
            set(("20"+lastDay.year).toInt(), lastDay.month.toInt()-1, lastDay.day.toInt())
            timeInMillis
        }.toString()

        return result
    }

    private fun getDayOfWeek(dayOfWeekString: String): DayOfWeek {
        return when(dayOfWeekString) {
            "Sunday" -> DayOfWeek.SUNDAY
            "Monday" -> DayOfWeek.MONDAY
            "Tuesday" -> DayOfWeek.TUESDAY
            "Wednesday" -> DayOfWeek.WEDNESDAY
            "Thursday" -> DayOfWeek.THURSDAY
            "Friday" -> DayOfWeek.FRIDAY
            "Saturday" -> DayOfWeek.SATURDAY
            else -> DayOfWeek.MONDAY
        }
    }

    private fun getNext(dayOfWeek: String, firstDay: DateHolder): LocalDate {
        val newDayOfWeek: DayOfWeek = getDayOfWeek(dayOfWeek)

        val today: LocalDate = LocalDate.of(("20"+firstDay.year).toInt(), firstDay.month.toInt()-1, firstDay.day.toInt())
        var next: LocalDate = today.plusDays(1)

        while (next.dayOfWeek !== newDayOfWeek) {
            next = next.plusDays(1)
        }

        return next
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