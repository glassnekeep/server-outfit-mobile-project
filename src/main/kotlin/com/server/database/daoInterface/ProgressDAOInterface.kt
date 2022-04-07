package com.server.database.daoInterface

import com.server.models.Program
import com.server.models.Progress
import com.server.models.User

interface ProgressDAOInterface {
    fun createProgress(
        program: Program,
        user: User,
        currentExercise: Int
    )
    fun updateProgress(
        id: Int,
        program: Program,
        user: User,
        currentExercise: Int
    )
    fun deleteProgress(id: Int)
    fun getProgressWithId(id: Int) : Progress?
    fun getProgressWithUserAndProgram(user: User, program: Program) : Progress?
    fun getProgressWithUserIdAndProgramId(userId: Int, programId: Int) : Progress?
}