package com.server.models

data class Progress(
    val program: Program,
    val user: User,
    val currentExercise: Int
)
