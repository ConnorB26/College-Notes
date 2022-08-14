package com.connorb26.notesapp.feature_note.domain.use_case

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.icu.util.Calendar
import android.provider.CalendarContract

class GetEventsDay {
    private val projection = arrayOf(
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.TITLE,
        CalendarContract.Instances.DESCRIPTION,
        CalendarContract.Instances.DTSTART,
        CalendarContract.Instances.DTEND,
        CalendarContract.Instances.ALL_DAY,
        CalendarContract.Instances.EVENT_LOCATION
    )

    operator fun invoke(contentResolver: ContentResolver, year: Int, month: Int, day: Int): Cursor? {
        val startTime: Calendar = Calendar.getInstance()
        startTime.set(year, month, day, 0, 0, 0)
        val endTime: Calendar = Calendar.getInstance()
        endTime.set(year, month, day , 23, 59, 59)

        val eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(eventsUriBuilder, startTime.timeInMillis)
        ContentUris.appendId(eventsUriBuilder, endTime.timeInMillis)
        val eventsUri = eventsUriBuilder.build()
        return contentResolver.query(eventsUri, projection, null, null, CalendarContract.Instances.DTSTART + " ASC")
    }
}