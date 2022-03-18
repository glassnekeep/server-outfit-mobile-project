package com.server.models
import kotlinx.serialization.Serializable

@Serializable
data class Calendar(
    //TODO Разобраться вот тут с типом данных для даты
    val date: String,
    val exercise: Exercise,
    val program: Program,
    val user: User
)
