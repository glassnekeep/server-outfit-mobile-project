package com.server.plugins

import com.server.database.dao.Dao
import com.server.routes.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

val password = "kirillleg17"
fun Application.configureRouting(dao: Dao) {
    registerUserRoutes(dao)
    registerCalendarRoutes(dao)
    registerExerciseRoutes(dao)
    registerProgramRoutes(dao)
    registerSettingsRoute(dao)
    registerProgressRoutes(dao)
    routing {
        get("/") {
            call.respondText("Everything is working correctly!!!")
        }
    }
}
