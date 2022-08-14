package com.connorb26.notesapp.feature_note.presentation.calendar

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connorb26.notesapp.feature_note.domain.model.Event
import com.connorb26.notesapp.feature_note.domain.model.EventType
import com.connorb26.notesapp.feature_note.domain.use_case.CalendarUseCases
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


@HiltViewModel
@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("StaticFieldLeak")
class CalendarViewModel @Inject constructor(
    private val calendarUseCases: CalendarUseCases,
    @ApplicationContext val context: Context
): ViewModel() {
    private val _state = mutableStateOf(CalendarState())
    val state: State<CalendarState> = _state

    private val contentResolver: ContentResolver = context.contentResolver

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.AddEvent -> {
                calendarUseCases.addEvent(context, event.startTime)
            }

            is CalendarEvent.AddHomework -> {
                calendarUseCases.addHW(context, event.startTime, event.classObj, "")
            }

            is CalendarEvent.EditEvent -> {
                calendarUseCases.editEvent(context, event.id)
            }

            is CalendarEvent.UpdateDayEvents -> {
                val events: MutableList<Event> = arrayListOf()
                val c: Cursor = calendarUseCases.getEventsDay(contentResolver, event.year, event.month, event.day)!!
                if(c.moveToFirst()) {
                    do {
                        events.add(
                            Event(
                                c.getLong(0),
                                if (c.getString(1) == null) "" else c.getString(1),
                                if (c.getString(2) == null) "" else c.getString(2),
                                c.getLong(3),
                                c.getLong(4),
                                c.getInt(5) == 1,
                                if (c.getString(6) == null) "" else c.getString(6)
                            )
                        )
                    } while(c.moveToNext())
                }
                _state.value = state.value.copy(
                    events = events
                )
            }
        }
    }
}