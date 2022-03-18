package com.server.database.daoInterface

import com.server.models.Calendar

interface CalendarDAOInterface: BaseDaoInterface {
    fun createCalendar(calendar: Calendar)
    fun updateCalendar(calendar: Calendar)
    fun deleteCalendar(calendar: Calendar)
}