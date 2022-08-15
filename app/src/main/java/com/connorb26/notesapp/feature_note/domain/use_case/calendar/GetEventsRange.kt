package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.CalendarContract
import android.icu.util.Calendar

class GetEventsRange {
    private val projection = arrayOf(
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.TITLE,
        CalendarContract.Instances.DESCRIPTION,
        CalendarContract.Instances.DTSTART,
        CalendarContract.Instances.DTEND,
        CalendarContract.Instances.ALL_DAY,
        CalendarContract.Instances.EVENT_LOCATION
    )

    operator fun invoke(contentResolver: ContentResolver, startTime: Calendar, endTime: Calendar): Cursor? {
        val eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(eventsUriBuilder, startTime.timeInMillis)
        ContentUris.appendId(eventsUriBuilder, endTime.timeInMillis)
        val eventsUri = eventsUriBuilder.build()
        return contentResolver.query(eventsUri, projection, null, null, CalendarContract.Instances.DTSTART + " ASC")
    }
}