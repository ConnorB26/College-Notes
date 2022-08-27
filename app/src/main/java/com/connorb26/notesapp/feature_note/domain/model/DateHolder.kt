package com.connorb26.notesapp.feature_note.domain.model

import android.icu.util.Calendar
import java.util.*

data class DateHolder (
    var day: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var default: String = "Date"
) {
    override fun toString(): String {
        return if(day == 0 && month == 0 && year == 0) {
            default
        } else {
            "${month+1}/$day/${year%2000}"
        }
    }

    companion object {
        fun compare(dateHolder1: DateHolder, dateHolder2: DateHolder): Boolean {
            val thisCal: Calendar = Calendar.getInstance()
            thisCal.set(Calendar.YEAR, dateHolder1.year)
            thisCal.set(Calendar.MONTH, dateHolder1.month)
            thisCal.set(Calendar.DAY_OF_MONTH, dateHolder1.day)

            val otherCal: Calendar = Calendar.getInstance()
            otherCal.set(Calendar.YEAR, dateHolder2.year)
            otherCal.set(Calendar.MONTH, dateHolder2.month)
            otherCal.set(Calendar.DAY_OF_MONTH, dateHolder2.day)

            return thisCal >= otherCal
        }

        fun addDay(dateHolder: DateHolder): DateHolder {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.YEAR, dateHolder.year)
            cal.set(Calendar.MONTH, dateHolder.month)
            cal.set(Calendar.DAY_OF_MONTH, dateHolder.day)
            cal.add(Calendar.DAY_OF_MONTH, 1)
            return create(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        }

        fun create(year: Int, month: Int, day: Int): DateHolder {
            return DateHolder(
                year = year,
                month = month,
                day = day
            )
        }
    }
}
