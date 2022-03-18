package com.server.models

data class User(
    val username: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val sex: String,
    val growth: Int,
    val weight: Int
)
