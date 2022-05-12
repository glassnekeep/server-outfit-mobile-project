package com.server.routes

import com.server.database.dao.Dao
import com.server.models.Progress
import com.server.models.SharedProgress
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
        get {
            val user = call.request.queryParameters["user"]
            user?.let {
                val progressList = dao.getProgressListWithUserId(userId = user.toInt())
                if (progressList.isNotEmpty()) {
                    call.respond(status = HttpStatusCode.OK, progressList)
                } else {
                    call.respondText("No progress found", status = HttpStatusCode.NotFound)
                }
            } ?: call.respondText(text = "Invalid parameters", status = HttpStatusCode.BadRequest)
        }
        post {
            val progress = call.receive<Progress>()
            dao.createProgress(progress.program, progress.user, progress.currentExercise)
            return@post call.respondText("Progress created successfully", status = HttpStatusCode.Created)
        }
        post("/share") {
//            val sender = call.request.queryParameters["sender"]
//            val recipient = call.request.queryParameters["recipient"]
//            val time = call.request.queryParameters["time"]
            val sharedProgress = call.receive<SharedProgress>()
            dao.runCatching {
                shareProgress(sharedProgress)
            }.onFailure {
                return@post call.respondText(status = HttpStatusCode.BadRequest, text = "Failed to share progress")
            }.onSuccess {
                return@post call.respondText(status = HttpStatusCode.OK, text = "Shared progress successfully")
            }
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
        put {
            val userId = call.request.queryParameters["user"]
            val programId = call.request.queryParameters["program"]
            if (userId != null && programId != null) {
                try {
                    dao.updateProgressWithUserIdAndProgramId(userId.toInt(), programId.toInt())
                    call.respondText(status = HttpStatusCode.OK, text = "Progress updated successfully")
                } catch (exception: Exception) {
                    call.respondText(status = HttpStatusCode.NotFound, text = "Not found progress")
                }
            } else {
                call.respondText(status = HttpStatusCode.BadRequest, text = "Invalid input")
            }
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