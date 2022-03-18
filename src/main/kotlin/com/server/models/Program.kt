package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val interval: Int,
    val exercise: List<Exercise>,
    val users: List<User>
)
