package com.server.database.daoInterface

import com.server.models.Calendar
import com.server.models.Program
import com.server.models.User

interface CalendarDAOInterface {
    fun createCalendar(
        date: String,
        program: Program,
        user: User
    )
    fun updateCalendar(
        id: Int,
        date: String,
        program: Program,
        user: User
    )
    fun deleteCalendar(id: Int)
    fun getCalendarWithId(id: Int) : Calendar?
    fun getCalendarListWithUser(userId: Int) : List<Calendar>?
}