package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract

class GetEventByID {
    private val projection = arrayOf(
        CalendarContract.Instances.EVENT_ID
    )

    operator fun invoke(context: Context, id: Long): Cursor? {
        val selection = "${CalendarContract.Instances.EVENT_ID} = ?"
        val selectionArgs: Array<String> = arrayOf(id.toString())

        return context.contentResolver.query(CalendarContract.Instances.CONTENT_URI, projection, selection, selectionArgs, null)
    }
}