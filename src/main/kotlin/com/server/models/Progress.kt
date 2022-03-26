package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val id: Int,
    val program: Program,
    val user: User,
    val currentExercise: Int
)
