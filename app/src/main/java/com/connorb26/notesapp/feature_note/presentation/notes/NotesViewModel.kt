package com.connorb26.notesapp.feature_note.presentation.notes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connorb26.notesapp.feature_note.domain.model.CalendarInst
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.domain.use_case.calendar.CalendarUseCases
import com.connorb26.notesapp.feature_note.domain.use_case.notes.NoteUseCases
import com.connorb26.notesapp.feature_note.domain.util.NoteOrder
import com.connorb26.notesapp.feature_note.domain.util.OrderType
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val calendarUseCases: CalendarUseCases,
    @ApplicationContext val context: Context
): ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private val _calendarInstances = mutableStateOf(listOf<CalendarInst>())
    val calendarInstances: State<List<CalendarInst>> = _calendarInstances

    private val _deleteDialog = mutableStateOf(false)
    val deleteDialog: State<Boolean> = _deleteDialog
    var deletingNote: Note? = null

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class &&
                        state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.DeleteDialogEnable -> {
                _deleteDialog.value = true
                deletingNote = event.note
            }
            is NotesEvent.DeleteDialogDisable -> {
                _deleteDialog.value = false
                deletingNote = null
            }
            is NotesEvent.GetCalendars -> {
                if(checkPermission()) {
                    _calendarInstances.value = calendarUseCases.getCalendars(context)
                }
            }
            is NotesEvent.NavigateToCalendar -> {
                if(checkPermission()) {
                    event.navController.navigate(Screen.CalendarScreen.route)
                }
            }
        }
    }

    fun checkPermission(): Boolean {
        val permission1 = Manifest.permission.READ_CALENDAR
        val res1: Int = context.checkCallingOrSelfPermission(permission1)
        val permission2 = Manifest.permission.WRITE_CALENDAR
        val res2: Int = context.checkCallingOrSelfPermission(permission2)

        return res1 == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}