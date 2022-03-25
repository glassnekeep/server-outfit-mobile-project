package com.server.database.daoInterface

import com.server.models.Exercise

interface ExerciseDAOInterface {
    fun createExercise(exercise: Exercise)
    fun updateExercise(exercise: Exercise)
    fun deleteExercise(exercise: Exercise)
}