package com.server.models
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Progress(
    val id: Int,
    val program: Program,
    val user: User,
    val currentExercise: Int
)

data class SharedProgress(
    val senderId: Int,
    val recipientId: Int,
    val time: Date
)
