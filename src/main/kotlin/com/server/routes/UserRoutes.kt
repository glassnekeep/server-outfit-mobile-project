package com.server.routes

import at.favre.lib.crypto.bcrypt.BCrypt
import com.server.database.dao.Dao
import com.server.models.User
import com.server.plugins.digestFunction
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
        call.respond(users)
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
        println("user.password, ${digestFunction(user.password)}")
        println("password, ${digestFunction("password").toString()}")
        call.respondText("User created successfully!", status = HttpStatusCode.OK)
    }
}

private fun Route.operateUser(dao: Dao) {
    route("/user") {
        get {
            val id = call.request.queryParameters["id"]
            val email = call.request.queryParameters["email"]
            val username = call.request.queryParameters["username"]
            val phoneNumber = call.request.queryParameters["phoneNumber"]
            if (id == null && email == null && username == null && phoneNumber == null) {
                call.respondText("No parameters", status = HttpStatusCode.BadRequest)
            }
            if (id != null) {
                val user = dao.getUserWithId(id.toInt())
                user?.let { call.respond(it) } ?: call.respondText("User does not exist", status = HttpStatusCode.BadRequest)
            }
            if (email != null) {
                val user = dao.getUserWithEmail(email)
                user?.let { call.respond(it) } ?: call.respondText("User does not exist", status = HttpStatusCode.BadRequest)
            }
            if (username != null) {
                val user = dao.getUserWithUsername(username)
                user?.let { call.respond(it) } ?: call.respondText("User does not exist", status = HttpStatusCode.BadRequest)
            }
            if (phoneNumber != null) {
                val user = dao.getUserWithPhoneNumber(phoneNumber)
                user?.let { call.respond(it) } ?: call.respondText("User does not exist", status = HttpStatusCode.BadRequest)
            }
        }
        put("/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val user = call.receive<User>()
                dao.updateUserWithId(
                    id.toInt(), user.username, user.firstname, user.lastname, user.phoneNumber, user.email,
                    user.password, user.sex, user.growth, user.weight)
                call.respondText("User updated successfully", status = HttpStatusCode.OK)
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
                call.respondText("User deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid id", status = HttpStatusCode.BadRequest)
            email?.let {
                dao.deleterUserWithEmail(email)
                call.respondText("User deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid email", status = HttpStatusCode.BadRequest)
            username?.let {
                dao.deleterUserWithUsername(username)
                call.respondText("User deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid username", status = HttpStatusCode.BadRequest)
            phoneNumber?.let {
                dao.deleterUserWithUsername(phoneNumber)
                call.respondText("User deleted successfully", status = HttpStatusCode.OK)
            } ?: call.respondText("Invalid phone number", status = HttpStatusCode.BadRequest)
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