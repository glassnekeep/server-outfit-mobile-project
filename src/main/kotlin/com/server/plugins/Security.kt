package com.server.plugins

import com.server.database.dao.Dao
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import io.ktor.util.*

fun Application.configureSecurity(dao: Dao) {
    val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }
    authentication {
        basic("auth-basic-hashed") {
            realm = "Access to the '/hash' path"
            validate { credentials ->
                val authenticatingUser = dao.getUserWithUsername(credentials.name)
                if (authenticatingUser != null && (authenticatingUser.password.equals(digestFunction(credentials.password)))) {
                    //print("credentials.name = ${credentials.name}, credentials.password = ${digestFunction(credentials.password)}")
                    UserIdPrincipal(credentials.name)
                } else {
                    print("NO SUCH USER, name = ${credentials.name}\n")
                    null
                }
            }
        }
    }

    routing {

    }
}
