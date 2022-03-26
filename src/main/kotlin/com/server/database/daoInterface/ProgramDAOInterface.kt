package com.server.database.daoInterface

import com.server.models.Exercise
import com.server.models.Program
import com.server.models.User

interface ProgramDAOInterface {
    fun createProgram(
        interval: Int,
        exercise: List<Exercise>,
        users: List<User>
    )
    fun updateProgram(
        id: Int,
        interval: Int,
        exercise: List<Exercise>,
        users: List<User>
    )
    fun deleteProgram(id: Int)
    fun getProgramWithId(id: Int)
    fun getProgramListWithUser(user: User) : List<Program>
    fun getUserListWithProgram(program: Program) : List<User>
}