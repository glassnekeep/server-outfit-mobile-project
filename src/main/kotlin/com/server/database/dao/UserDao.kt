package com.server.database.dao

import com.server.database.daoInterface.UserDAOInterface
import com.server.database.tables.UserTable
import com.server.models.User
import jdk.jshell.execution.Util
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao(val db: Database) : UserDAOInterface {
    override fun createUser(
        username: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        email: String,
        password: String,
        sex: String,
        growth: Int,
        weight: Int
    ) = transaction(db) {
        UserTable.insert {
            it[UserTable.username] = username
            it[UserTable.firstname] = firstname
            it[UserTable.lastname] = lastname
            it[UserTable.phoneNumber] = phoneNumber
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.sex] = sex
            it[UserTable.growth] = growth
            it[UserTable.weight] = weight
        }
    }

    override fun updateUserWithId(
        id: Int,
        username: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        email: String,
        password: String,
        sex: String,
        growth: Int,
        weight: Int
    ) = transaction(db) {
        UserTable.update {
            it[UserTable.id] = id
            it[UserTable.username] = username
            it[UserTable.firstname] = firstname
            it[UserTable.lastname] = lastname
            it[UserTable.phoneNumber] = phoneNumber
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.sex] = sex
            it[UserTable.growth] = growth
            it[UserTable.weight] = weight
        }
    }

    override fun deleteUserWithId(id: Int) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.id eq id
        }
        Unit
    }

    override fun deleterUserWithEmail(email: String) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.email eq email
        }
        Unit
    }

    override fun deleterUserWithUsername(username: String) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.username eq username
        }
        Unit
    }

    override fun deleteUserWithPhoneNumber(phoneNumber: String) {
        UserTable.deleteWhere {
            UserTable.phoneNumber eq phoneNumber
        }
        Unit
    }

    override fun getUserWithId(id: Int): User? = transaction(db) {
        UserTable.select { UserTable.id eq id }.map {
            User(
                it[UserTable.id],
                it[UserTable.username],
                it[UserTable.firstname],
                it[UserTable.lastname],
                it[UserTable.phoneNumber],
                it[UserTable.email],
                it[UserTable.password],
                it[UserTable.sex],
                it[UserTable.growth],
                it[UserTable.weight]
            )
        }.singleOrNull()
    }

    override fun getUserWithEmail(email: String): User {
        TODO("Not yet implemented")
    }

    override fun getUserWithUsername(username: String): User {
        TODO("Not yet implemented")
    }

    override fun getUserWithPhoneNumber(phoneNumber: String): User {
        TODO("Not yet implemented")
    }
}