package com.server.routes

import com.server.database.dao.Dao
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.operateProgram(dao: Dao) {
    route("/program") {
        get("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            val program = dao.getProgramWithId(id)
            program?.let {
                return@get call.respond(status = HttpStatusCode.OK, program)
            } ?: return@get call.respondText("Program not found", status = HttpStatusCode.NotFound)
        }
        get("/user/{id}") {
            val userId = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            val programList = dao.getProgramListWithUserId(userId)
            if (programList.isNotEmpty()) {
                return@get call.respond(status = HttpStatusCode.OK, programList)
            } else {
                return@get call.respondText(
                    "No users connected to this program", status = HttpStatusCode.NotFound)
            }
        }
    }
}

fun Application.registerProgramRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateProgram(dao)
        }
    }
}