package com.server.routes

import com.server.database.dao.Dao
import com.server.models.Settings
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

private fun Route.operateSettings(dao: Dao) {
    route("/settings") {
        get {
            val id = call.request.queryParameters["id"]
            val userId = call.request.queryParameters["user"]
            if (id != null || userId != null) {
                id?.let {
                    val settings = dao.getSettingsWithId(it.toInt())
                    settings?.let {
                        call.respond(status = HttpStatusCode.OK, settings)
                    } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                } ?: userId?.let {
                    val settings = dao.getSettingsWithUser(dao.getUserWithId(it.toInt())!!)
                    settings?.let {
                        call.respond(status = HttpStatusCode.OK, settings)
                    } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                }
            } else {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
            }
        }
        post {
            val settings = call.receive<Settings>()
            dao.createSettings(settings.user, settings.restTime, settings.countDownTime)
            call.respondText("Settings created successfully", status = HttpStatusCode.OK)
        }
        put("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val settings = call.receive<Settings>()
                dao.updateSettings(id.toInt(), settings.user, settings.restTime, settings.countDownTime)
                call.respondText("Settings updated successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        delete {
            val id = call.request.queryParameters["id"]
            val userId = call.request.queryParameters["user"]
            if (id != null || userId != null) {
                id?.let {
                    dao.deleteSettingsWithId(id.toInt())
                } ?: userId?.let {
                    val user = dao.getUserWithId(userId.toInt())
                    if (user != null) {
                        dao.deleteSettingsWithUser(user)
                    } else {
                        call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
                    }
                } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            } else {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

fun Application.registerSettingsRoute(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateSettings(dao)
        }
    }
}