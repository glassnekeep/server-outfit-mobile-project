package com.server.routes

import com.server.database.dao.Dao
import com.server.models.Program
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.getAllPrograms(dao: Dao) {
    route("/programs") {
        get {
            val programList = dao.getAllPrograms()
            if (programList.isNotEmpty()) {
                call.respond(status = HttpStatusCode.OK, programList)
            } else {
                call.respondText("No programs exist", status = HttpStatusCode.NotFound)
            }
        }
    }
}

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
                    "No programs connected to this user", status = HttpStatusCode.NotFound)
            }
        }
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@get call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            val userList = dao.getUserListWithProgramId(id)
            if (userList.isNotEmpty()) {
                return@get call.respond(status = HttpStatusCode.OK, userList)
            } else {
                return@get call.respondText(
                    "No users connected to this program", status = HttpStatusCode.NotFound
                )
            }
        }
        post {
            val program = call.receive<Program>()
            program.let {
                dao.createProgram(it.name, it.interval, it.exercise, it.users, it.image)
            }
            call.respondText("Program created successfully", status = HttpStatusCode.Created)
        }
        post("/new/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@post call.respondText(
                "Missing of malformed id", status = HttpStatusCode.BadRequest
            )
            val exercise = call.request.queryParameters["exercise"]?.toInt()
            val user = call.request.queryParameters["user"]?.toInt()
            if (exercise != null) {
                dao.addExerciseToProgram(exercise, id)
                return@post call.respondText(
                    "Exercise successfully added to the program", status = HttpStatusCode.Accepted)
            }
            if (user != null) {
                dao.addUserToProgram(id, user)
                return@post call.respondText(
                    "User successfully added to the program", status = HttpStatusCode.Accepted
                )
            }
            call.respondText("Missing or malformed query parameters", status = HttpStatusCode.BadRequest)
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@put call.respondText(
                "Invalid or malformed id",
                status = HttpStatusCode.BadRequest
            )
            id.let {
                val program = call.receive<Program>()
                dao.updateProgram(id, program.name, program.interval, program.exercise, program.users, program.image)
                return@put call.respondText("program updated successfully", status = HttpStatusCode.Accepted)
            }
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@delete call.respondText(
                "Missing or malformed id", status = HttpStatusCode.BadRequest
            )
            //TODO здесь и в подобных местах нужно проверять что такой пользователь есть в базе данных и отправлять соответствующий response
            dao.deleteProgram(id)
            call.respondText("Program deleted successfully", status = HttpStatusCode.Accepted)
        }
    }
}

fun Application.registerProgramRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateProgram(dao)
        }
        getAllPrograms(dao)
    }
}