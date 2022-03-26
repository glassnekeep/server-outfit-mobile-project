package com.server.database.daoInterface

import com.server.models.Exercise
import com.server.models.Program

interface ExerciseDAOInterface {
    fun createExercise(
        time: String,
        numberOfApproaches: Int,
        periods: Int,
        weight: Int,
        image: String
    )
    fun updateExercise(
        id: Int,
        time: String,
        numberOfApproaches: Int,
        periods: Int,
        weight: Int,
        image: String
    )
    fun deleteExercise(id: Int)
    fun getExercise(id: Int) : Exercise
    fun getExerciseListWIthProgram(program: Program) : List<Exercise>
}