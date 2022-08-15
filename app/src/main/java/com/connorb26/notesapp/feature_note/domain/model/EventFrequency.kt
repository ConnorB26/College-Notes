package com.connorb26.notesapp.feature_note.domain.model

enum class EventFrequency(val frequency: String) {
    ONCE("FREQ=ONCE"),
    DAILY("FREQ=DAILY"),
    WEEKLY("FREQ=WEEKLY"),
    MONTHLY("FREQ=MONTHLY"),
    BY_SUNDAY(";BYDAY=SU"),
    BY_MONDAY(";BYDAY=MO"),
    BY_TUESDAY(";BYDAY=TY"),
    BY_WEDNESDAY(";BYDAY=WE"),
    BY_THURSDAY(";BYDAY=TH"),
    BY_FRIDAY(";BYDAY=FR"),
    BY_SATURDAY(";BYDAY=SA"),
    UNTIL(";UNTIL="),
    COUNT(";COUNT=")
}