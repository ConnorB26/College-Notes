package com.connorb26.notesapp.feature_note.domain.model

data class CalendarInst (
    val id: Long = 0,
    val emailAddress: String = ""
)  {
    override fun toString(): String {
        return "Calendar $id = $emailAddress"
    }

    companion object {
        const val selCalPrefName = "Selected Calendar"
    }
}