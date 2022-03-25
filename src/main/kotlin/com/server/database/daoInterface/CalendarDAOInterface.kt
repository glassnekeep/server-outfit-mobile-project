package com.server.database.daoInterface

import com.server.models.Calendar

interface CalendarDAOInterface {
    fun createCalendar(calendar: Calendar)
    fun updateCalendar(calendar: Calendar)
    fun deleteCalendar(calendar: Calendar)
    fun
}