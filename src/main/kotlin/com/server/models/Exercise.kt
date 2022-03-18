package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val time: String,
    val numberOfApproaches: Int,
    val periods: Int,
    //TODO Подумать над этим полем, возможно стоит убрать, так это вес того или иного упражнения для отсчета прогресса
    val weight: Int,
    val image: String
)
