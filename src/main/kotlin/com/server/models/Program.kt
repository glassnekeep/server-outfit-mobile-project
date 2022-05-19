package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val id: Int,
    val name: String,
    val interval: Int,
    val exercise: List<Exercise>,
    val users: List<User>,
    val image: String,
    val numberOfExercises: Int
    //TODO подумать о том чтобы тут было максимальное число очков за данную программу
)
