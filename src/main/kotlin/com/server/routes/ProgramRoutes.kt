package com.server.routes

import com.server.database.dao.Dao
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*

private fun Route.operateProgram(dao: Dao) {
    route("/program") {

    }
}

fun Application.registerProgramRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateProgram(dao)
        }
    }
}