package com.connorb26.notesapp.feature_note.domain.model

import android.icu.util.Calendar
import java.util.*

data class TimeHolder (
    var hour: Int = 0,
    var minute: Int = 0,
    var ampm: String = "",
    var default: String = "Time"
) {
    override fun toString(): String {
        return if(hour == 0 && minute == 0 && ampm.isBlank()) {
            default
        } else {
            "$hour:${if(minute < 10) "0$minute" else minute} $ampm"
        }
    }

    companion object {
        fun compare(timeHolder1: TimeHolder, timeHolder2: TimeHolder): Boolean {
            val thisNewHour: Int = if(timeHolder1.ampm == "PM") timeHolder1.hour+12 else timeHolder1.hour
            val otherNewHour: Int = if(timeHolder2.ampm == "PM") timeHolder2.hour+12 else timeHolder2.hour

            val thisCal: Calendar = Calendar.getInstance()
            thisCal.set(Calendar.HOUR_OF_DAY, thisNewHour)
            thisCal.set(Calendar.MINUTE, timeHolder1.minute)

            val otherCal: Calendar = Calendar.getInstance()
            otherCal.set(Calendar.HOUR_OF_DAY, otherNewHour)
            otherCal.set(Calendar.MINUTE, timeHolder2.minute)

            return thisCal > otherCal
        }

        fun addHour(timeHolder: TimeHolder): TimeHolder {
            val hour = timeHolder.hour+1
            val minute = timeHolder.minute
            return create(hour, minute)
        }

        fun create(hour: Int, minute: Int): TimeHolder {
            return TimeHolder(
                hour = if (hour % 12 == 0) 12 else (hour % 12),
                minute = minute,
                ampm = if(hour < 12) "AM" else "PM"
            )
        }

        fun createDefault(defaultString: String): TimeHolder {
            return TimeHolder(default = defaultString)
        }
    }
}