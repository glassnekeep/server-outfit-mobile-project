package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val id: Int,
    val user: User,
    val restTime: Int,
    val countDownTime: Int
)
