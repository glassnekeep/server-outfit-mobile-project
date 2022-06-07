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
                url = "postgres://kctsqhltbshhfv:0bf6b42b3a2e2d334c47f14840d50e7b3d830b49d69502e14373e8304a633cd0@ec2-34-201-95-176.compute-1.amazonaws.com:5432/d7pgkl27o587ff",
                driver = "org.postgresql.Driver",
                user = "kctsqhltbshhfv",
                password = "0bf6b42b3a2e2d334c47f14840d50e7b3d830b49d69502e14373e8304a633cd0"
        ))
        dao.init()
        configureSecurity(dao)
        configureRouting(dao)
        configureSerialization()
    }.start(wait = true)
}
