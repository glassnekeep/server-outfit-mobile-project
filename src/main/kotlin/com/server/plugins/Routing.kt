package com.server.plugins

import com.server.database.dao.Dao
import com.server.routes.registerUserRoutes
import io.ktor.application.*

val password = "kirillleg17"
fun Application.configureRouting(dao: Dao) {
    registerUserRoutes(dao)
}
