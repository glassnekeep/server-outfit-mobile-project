package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val id: Int,
    val interval: Int,
    val exercise: List<Exercise>,
    val users: List<User>
    //TODO подумать о том чтобы тут было максимальное число очков за данную программу
)
