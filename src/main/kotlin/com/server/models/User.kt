package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
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
