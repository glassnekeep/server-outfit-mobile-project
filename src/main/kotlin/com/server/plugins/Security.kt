package com.server.plugins

import com.server.database.dao.Dao
import com.server.models.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.util.*

val digestFunction = getDigestFunction("SHA-256") { "ktor${it.length}" }

fun Application.configureSecurity(dao: Dao) {
    authentication {
        basic("auth-basic-hashed") {
            realm = "Access to the '/user' path"
            validate { credentials ->
                val authenticatingUser: User? = dao.getUserWithUsername(credentials.name)
                /*if ((authenticatingUser != null) && (BCrypt.verifyer().verify(
                        credentials.password.toByteArray(
                            StandardCharsets.UTF_8
                        ), authenticatingUser.password.toByteArray(StandardCharsets.UTF_8)
                    ).verified)
                )*/ if (authenticatingUser != null && credentials.password == authenticatingUser.password) {
                    //print("credentials.name = ${credentials.name}, credentials.password = ${digestFunction(credentials.password)}")
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
