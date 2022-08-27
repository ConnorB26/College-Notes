package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.connorb26.notesapp.feature_note.domain.model.CalendarInst

class GetCalendars {
    private val projection: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,
        CalendarContract.Calendars.ACCOUNT_NAME,
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
    )

    operator fun invoke(context: Context): List<CalendarInst> {
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val cur: Cursor = context.contentResolver.query(uri, projection, null, null, null)!!

        val calList = mutableListOf<CalendarInst>()
        while(cur.moveToNext()) {
            if(cur.getString(1) == cur.getString(2)) {
                calList.add(CalendarInst(
                    id = cur.getLong(0),
                    emailAddress = cur.getString(1)
                ))
            }
        }
        return calList
    }
}