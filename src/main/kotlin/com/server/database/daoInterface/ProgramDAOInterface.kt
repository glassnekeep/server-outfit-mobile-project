package com.server.database.daoInterface

import com.server.models.Exercise
import com.server.models.Program
import com.server.models.User

interface ProgramDAOInterface {
    fun createProgram(
        interval: Int,
        exercises: List<Exercise>,
        users: List<User>
    )
    fun createEmptyProgram(interval: Int)
    fun updateProgram(
        id: Int,
        interval: Int,
        exercises: List<Exercise>,
        users: List<User>
    )
    fun deleteProgram(id: Int)
    fun getProgramWithId(id: Int) : Program?
    fun getProgramListWithUser(user: User) : List<Program>
    fun getUserListWithProgram(program: Program) : List<User>
    fun getUserListWithProgramId(id: Int) : List<User>?
    fun addUserToProgram(programId: Int, userId: Int)
    fun addExerciseToProgram(exerciseId: Int, programId: Int)
}