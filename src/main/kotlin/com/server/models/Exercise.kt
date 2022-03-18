package com.server.models

data class Exercise(
    val time: Int,
    val numberOfApproaches: Int,
    val periods: Int,
    //TODO Подумать над этим полем, возможно стоит убрать, так это вес того или иного упражнения для отсчета прогресса
    val weight: Int,
    val image: String
)
