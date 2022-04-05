package com.server.plugins

import com.server.database.dao.Dao
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*

val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }

fun Application.configureSecurity(dao: Dao) {
    authentication {
        basic("auth-basic-hashed") {
            realm = "Access to the '/user' path"
            validate { credentials ->
                val authenticatingUser = dao.getUserWithUsername(credentials.name)
                if (authenticatingUser != null && (authenticatingUser.password == credentials.password)) {
                    //print("credentials.name = ${credentials.name}, credentials.password = ${digestFunction(credentials.password)}")
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    routing {

    }
}
