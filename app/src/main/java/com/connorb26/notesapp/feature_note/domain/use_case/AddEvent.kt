package com.connorb26.notesapp.feature_note.domain.use_case

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract

class AddEvent {
    operator fun invoke(context: Context, startTime: Long) {
        context.startActivity(
            Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 3600000)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}