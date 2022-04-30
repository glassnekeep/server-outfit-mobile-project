package com.server.database.daoInterface

import com.server.models.Exercise
import com.server.models.Program
import com.server.models.User

interface ProgramDAOInterface {
    fun createProgram(
        name: String,
        interval: Int,
        exercises: List<Exercise>,
        users: List<User>,
        image: String
    )
    fun createEmptyProgram(interval: Int)
    fun updateProgram(
        id: Int,
        name: String,
        interval: Int,
        exercises: List<Exercise>,
        users: List<User>,
        image: String
    )
    fun deleteProgram(id: Int)
    fun getProgramWithId(id: Int) : Program?
    fun getProgramListWithUser(user: User) : List<Program>
    fun getUserListWithProgram(program: Program) : List<User>
    fun getUserListWithProgramId(id: Int) : List<User>?
    fun getProgramListWithUserId(userId: Int) : List<Program>
    fun getAllPrograms() : List<Program>
    fun addUserToProgram(programId: Int, userId: Int)
    fun addExerciseToProgram(exerciseId: Int, programId: Int)
    fun deleteUserFromProgram(userId: Int, programId: Int)
    fun deleteExerciseFromProgram(exerciseId: Int, programId: Int)
}