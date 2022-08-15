package com.connorb26.notesapp.feature_note.domain.use_case.calendar

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract

class EditEvent {
    operator fun invoke(context: Context, id: Long) {
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id)
        context.startActivity(Intent(Intent.ACTION_VIEW)
            .setData(uri)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}