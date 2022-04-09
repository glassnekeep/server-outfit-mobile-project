package com.server.routes

import com.server.database.dao.Dao
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.operateCalendar(dao: Dao) {
    route("/calendar") {
        get {
            val id = call.request.queryParameters["id"]
            id?.let {
                val calendar = dao.getCalendarWithId(id.toInt())
                calendar?.let {
                    call.respond(status = HttpStatusCode.OK, calendar)
                } ?: call.respondText("Calendar not found", status = HttpStatusCode.BadRequest)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            val userId = call.request.queryParameters["user"]
            userId?.let {
                val calendarList = dao.getCalendarListWithUser(userId.toInt())
                 if (calendarList.isNotEmpty()) {
                    call.respond(status = HttpStatusCode.OK, calendarList)
                 } else { call.respondText("No calendars found") }
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        post {
            val calendar = call.receive<com.server.models.Calendar>()
            dao.createCalendar(calendar.date, calendar.program, calendar.user)
            call.respondText("Calendar created successfully", status = HttpStatusCode.OK)
        }
        put("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val calendar = call.receive<com.server.models.Calendar>()
                dao.updateCalendar(id.toInt(), calendar.date, calendar.program, calendar.user)
                call.respondText("Calendar updated successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                dao.deleteCalendar(it.toInt())
                call.respondText("Calendar deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
    }
}

fun Application.registerCalendarRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateCalendar(dao)
        }
    }
}