package com.server.database.dao

import com.server.database.daoInterface.CalendarDAOInterface
import com.server.database.daoInterface.UserDAOInterface
import com.server.models.User
import org.jetbrains.exposed.sql.Database

class UserDao(val db: Database): UserDAOInterface {

    override fun createUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String) {
        TODO("Not yet implemented")
    }
}