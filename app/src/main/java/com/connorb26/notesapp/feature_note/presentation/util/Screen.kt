package com.connorb26.notesapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
    object CalendarScreen: Screen("calendar_screen")
    object ClassScreen: Screen("classes_screen")
    object AddEditClassScreen: Screen("add_edit_class_screen")
}
