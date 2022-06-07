package com.server

import com.server.database.dao.Dao
import com.server.plugins.configureRouting
import com.server.plugins.configureSecurity
import com.server.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt()) {
        val dao = Dao(
            Database.connect(
                url = System.getenv("DATABASE_URL"),
                driver = "org.postgresql.Driver",
            )
        )
        dao.init()
        configureSecurity(dao)
        configureRouting(dao)
        configureSerialization()
    }.start(wait = true)
}

