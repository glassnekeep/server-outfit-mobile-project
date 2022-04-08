package com.server.routes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.server.database.dao.Dao
import com.server.models.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.nio.charset.StandardCharsets

private fun Route.getAllUsersIds(dao: Dao) {
    get("/users") {
        val users: List<Int> = dao.getAllUsersId()
        if (users.isNotEmpty()) {
            call.respond(users)
        } else {
            call.respondText("No user ids found", status = HttpStatusCode.NotFound)
        }
    }
}

private fun Route.createUser(dao: Dao) {
    post("/user") {
        val user = call.receive<User>()
        val encryptedPassword = BCrypt.withDefaults().hash(10, user.password.toByteArray(StandardCharsets.UTF_8))
        dao.createUser(
            user.username, user.firstname, user.lastname, user.phoneNumber, user.email,
            user.password, user.sex, user.growth, user.weight
        )
        call.respondText("User created successfully!", status = HttpStatusCode.Created)
    }
}

private fun Route.operateUser(dao: Dao) {
    route("/user") {
        get {
            val id = call.request.queryParameters["id"]?.toInt()
            val email = call.request.queryParameters["email"]
            val username = call.request.queryParameters["username"]
            val phoneNumber = call.request.queryParameters["phoneNumber"]
            if (id == null && email == null && username == null && phoneNumber == null) {
                call.respondText("No parameters", status = HttpStatusCode.BadRequest)
            }
            if (id != null) {
                val user = dao.getUserWithId(id.toInt())
                user?.let { call.respond(status = HttpStatusCode.OK, it) } ?: call.respondText("User does not exist", status = HttpStatusCode.NotFound)
            }
            if (email != null) {
                val user = dao.getUserWithEmail(email)
                user?.let { call.respond(status = HttpStatusCode.OK, it) } ?: call.respondText("User does not exist", status = HttpStatusCode.NotFound)
            }
            if (username != null) {
                val user = dao.getUserWithUsername(username)
                user?.let { call.respond(status = HttpStatusCode.OK, it) } ?: call.respondText("User does not exist", status = HttpStatusCode.NotFound)
            }
            if (phoneNumber != null) {
                val user = dao.getUserWithPhoneNumber(phoneNumber)
                user?.let { call.respond(status = HttpStatusCode.OK, it) } ?: call.respondText("User does not exist", status = HttpStatusCode.NotFound)
            }
        }
        put("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val user= call.receive<User>()
                dao.updateUserWithId(
                    id.toInt(), user.username, user.firstname, user.lastname, user.phoneNumber, user.email,
                    user.password, user.sex, user.growth, user.weight)
                call.respondText("User updated successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
        }
        delete {
            val id = call.request.queryParameters["id"]
            val email = call.request.queryParameters["email"]
            val username = call.request.queryParameters["username"]
            val phoneNumber = call.request.queryParameters["phoneNumber"]
            if (id == null && email == null && username == null && phoneNumber == null) {
                call.respondText("No parameters", status = HttpStatusCode.BadRequest)
            }
            id?.let {
                dao.deleteUserWithId(id.toInt())
                call.respondText("User deleted successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.NotFound)
            email?.let {
                dao.deleterUserWithEmail(email)
                call.respondText("User deleted successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid email", status = HttpStatusCode.NotFound)
            username?.let {
                dao.deleterUserWithUsername(username)
                call.respondText("User deleted successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid username", status = HttpStatusCode.NotFound)
            phoneNumber?.let {
                dao.deleterUserWithUsername(phoneNumber)
                call.respondText("User deleted successfully", status = HttpStatusCode.Accepted)
            } ?: call.respondText("Invalid phone number", status = HttpStatusCode.NotFound)
        }
    }
}

fun Application.registerUserRoutes(dao: Dao) {
    routing {
        authenticate("auth-basic-hashed") {
            operateUser(dao)
        }
        createUser(dao)
        getAllUsersIds(dao)
    }
}