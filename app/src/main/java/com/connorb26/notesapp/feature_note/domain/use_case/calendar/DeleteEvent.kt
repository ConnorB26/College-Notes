package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract

class DeleteEvent {
    operator fun invoke(context: Context, id: Long) {
        val deleteUri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id)
        context.contentResolver.delete(deleteUri, null, null)
    }
}