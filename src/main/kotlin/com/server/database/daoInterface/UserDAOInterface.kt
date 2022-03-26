package com.server.database.daoInterface

import com.server.models.User

interface UserDAOInterface {
    fun createUser(
        username: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        email: String,
        password: String,
        sex: String,
        growth: Int,
        weight: Int
    )
    fun updateUserWithId(
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
    )
    fun deleteUserWithId(id: Int)
    fun deleterUserWithEmail(email: String)
    fun deleterUserWithUsername(username: String)
    fun deleteUserWithPhoneNumber(phoneNumber: String)
    fun getUserWithId(id: Int) : User
    fun getUserWithEmail(email: String) : User
    fun getUserWithUsername(username: String) : User
    fun getUserWithPhoneNumber(phoneNumber: String) : User
}