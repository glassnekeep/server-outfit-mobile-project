package com.server.database

import com.server.models.User

interface UserDAOInterface {
    fun init()
    fun createUser(user: User)
    fun updateUser(user: User)
    fun deleteUser(user: User)
    fun getUserByEmail(email: String)
}