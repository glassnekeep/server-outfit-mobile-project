package com.server.models

data class Program(
    val interval: Int,
    val exercise: List<Exercise>,
    val users: List<User>
)
