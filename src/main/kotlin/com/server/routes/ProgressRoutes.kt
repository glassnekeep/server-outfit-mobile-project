package com.server.routes

import com.server.database.dao.Dao
import com.server.models.Progress
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.operateProgress(dao: Dao) {
    route("/progress") {
        get("/{id?}") {
            val id = call.parameters["id"]
            val progress: Progress? = if(id != null) {
                dao.getProgressWithId(id.toInt())
            } else {
                val userId = call.request.queryParameters["user"]
                val programId = call.request.queryParameters["program"]
                if (userId != null && programId != null) {
                    dao.getProgressWithUserIdAndProgramId(userId.toInt(), programId.toInt())
                } else {
                    null
                }
            }
            progress?.let {
                call.respond(status = HttpStatusCode.OK, progress)
            } ?: call.respondText("Missing or invalid id", status = HttpStatusCode.BadRequest)
        }
        post {
            val progress = call.receive<Progress>()
            dao.createProgress(progress.program, progress.user, progress.currentExercise)
            call.respondText("Progress created successfully", status = HttpStatusCode.Created)
        }
        put("/{id}") {
            val id = call.parameters["id"]?.toInt() ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val progress = call.receive<Progress>()
            dao.updateProgress(id, progress.program, progress.user, progress.currentExercise)
            call.respondText("Progress updated successfully", status = HttpStatusCode.Accepted)
        }
        delete("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                dao.deleteProgress(it.toInt())
                call.respondText("Progress deleted successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
    }
}

fun Application.registerProgressRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateProgress(dao)
        }
    }
}