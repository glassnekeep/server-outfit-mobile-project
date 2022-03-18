package com.server.database.daoInterface

import com.server.models.Exercise

interface ExerciseDAOInterface: BaseDaoInterface {
    fun createExercise(exercise: Exercise)
    fun updateExercise(exercise: Exercise)
    fun deleteExercise(exercise: Exercise)
}