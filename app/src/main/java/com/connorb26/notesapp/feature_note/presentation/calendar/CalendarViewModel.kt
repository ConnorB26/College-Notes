package com.connorb26.notesapp.feature_note.presentation.calendar

import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connorb26.notesapp.feature_note.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
@RequiresApi(Build.VERSION_CODES.N)
class CalendarViewModel @Inject constructor(
): ViewModel() {
    private val _state = mutableStateOf(CalendarState())
    val state: State<CalendarState> = _state

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.AddEvent -> {

            }
            is CalendarEvent.UpdateDayEvents -> {
                var events: MutableList<Event> = arrayListOf()
                val c: Cursor = event.cursor!!
                if(c.moveToFirst()) {
                    do {
                        events.add(
                            Event(
                                c.getLong(0),
                                c.getString(1),
                                c.getString(2),
                                c.getLong(3),
                                c.getLong(4),
                                c.getInt(5) == 1,
                                c.getString(6)
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