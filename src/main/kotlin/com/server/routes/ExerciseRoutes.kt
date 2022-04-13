package com.server.routes

import com.server.database.dao.Dao
import com.server.models.Exercise
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.operateExercise(dao: Dao) {
    route("/exercise") {
        get {
            val id = call.request.queryParameters["id"]
            id?.let {
                val exercise = dao.getExercise(id.toInt())
                exercise?.let {
                    call.respond(status = HttpStatusCode.OK, exercise)
                } ?: call.respondText("Exercise not found", status = HttpStatusCode.BadRequest)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            val programId = call.request.queryParameters["program"]
            programId?.let {
                val exerciseList = dao.getExerciseListWithProgramId(programId.toInt())
                if (exerciseList.isNotEmpty()) {
                    call.respond(status = HttpStatusCode.OK, exerciseList)
                } else { call.respondText("No calendars found") }
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        post {
            val exercise = call.receive<Exercise>()
            dao.createExercise(exercise.name, exercise.time, exercise.numberOfApproaches, exercise.periods, exercise.weight, exercise.image)
            call.respondText("Exercise created successfully", status = HttpStatusCode.OK)
        }
        put("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val exercise = call.receive<Exercise>()
                dao.updateExercise(id.toInt(), exercise.name, exercise.time, exercise.numberOfApproaches, exercise.periods, exercise.weight, exercise.image)
                call.respondText("Exercise updated successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                dao.deleteExercise(it.toInt())
                call.respondText("Exercise deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
    }
}

fun Application.registerExerciseRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateExercise(dao)
        }
    }
}