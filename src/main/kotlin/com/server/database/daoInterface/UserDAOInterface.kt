package com.server.database.daoInterface

import com.server.models.User

interface UserDAOInterface {
    fun createUser(user: User)
    fun updateUser(user: User)
    fun deleteUser(user: User)
    fun getUserByEmail(email: String)
}