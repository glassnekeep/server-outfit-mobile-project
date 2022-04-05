package com.server.routes

import com.server.database.dao.Dao
import com.server.models.User
import com.server.plugins.digestFunction
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createUser(dao: Dao) {
    post("/user") {
        val user = call.receive<User>()
        dao.createUser(
            user.username, user.firstname, user.lastname, user.phoneNumber, user.email,
            user.password, user.sex, user.growth, user.weight
        )
        println("user.password, ${digestFunction(user.password).toString()}")
        println("password, ${digestFunction("password").toString()}")
        call.respond("User created successfully!")
    }
}

fun Route.operateUser(dao: Dao) {
    get("/user/id/{id}") {
        val id = call.parameters["id"]
    }
    get("/user/email/{email}") {

    }
    get("/user/username/{username}") {

    }
    get("/user/phoneNumber/{phoneNumber}") {

    }
    get("/user") {
        val id = call.request.queryParameters["id"]
        val email = call.request.queryParameters["email"]
        val username = call.request.queryParameters["username"]
        val phoneNumber = call.request.queryParameters["phoneNumber"]
        if (id != null) {
            call.respond(mapOf("user" to dao.getUserWithId(id.toInt())))
        }
        if (email != null) {
            call.respond(mapOf("user" to dao.getUserWithEmail(email)))
        }
        if (username != null) {
            call.respond(mapOf("user" to dao.getUserWithUsername(username)))
        }
        if (phoneNumber != null) {
            call.respond(mapOf("user" to dao.getUserWithPhoneNumber(phoneNumber)))
        }
    }
    put("/user/{id}") {
        val id = call.parameters["id"]
        id?.let {
            val user = call.receive<User>()
            dao.updateUserWithId(
                id.toInt(), user.username, user.firstname, user.lastname, user.phoneNumber, user.email,
                user.password, user.sex, user.growth, user.weight)
            call.respondText("User updated successfully!", status = HttpStatusCode.OK)
        } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
    }
    delete("/user/{id}") {
        val id = call.parameters["id"]
        id?.let {
            dao.deleteUserWithId(id.toInt())
            call.respondText("User deleted successfully!", status = HttpStatusCode.OK)
        } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
    }
}

fun Application.registerUserRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateUser(dao)
        }
        createUser(dao)
    }
}