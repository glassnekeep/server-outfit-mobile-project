package com.server

import com.server.database.dao.Dao
import com.server.plugins.configureRouting
import com.server.plugins.configureSecurity
import com.server.plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        val dao = Dao(
            Database.connect(
            url = "jdbc:postgresql://localhost:5432/postgres",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = com.server.plugins.password
        ))
        dao.init()
        configureRouting(dao)
        configureSecurity(dao)
        configureSerialization()
    }.start(wait = true)
}
