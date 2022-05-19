package com.server.database.daoInterface

import com.server.models.Program
import com.server.models.Progress
import com.server.models.SharedProgress
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
    fun updateProgressWithUserIdAndProgramId(
        userId: Int, programId: Int
    )
    //fun shareProgress(senderId: Int, recipientId: Int, time: String)
    fun shareProgress(sharedProgress: SharedProgress)
    fun deleteProgress(id: Int)
    fun getProgressWithId(id: Int) : Progress?
    fun getSharedProgressWithUserId(id: Int) : List<List<Progress>>
    fun getProgressWithUserAndProgram(user: User, program: Program) : Progress?
    fun getProgressWithUserIdAndProgramId(userId: Int, programId: Int) : Progress?
    fun getProgressListWithUserId(userId: Int) : List<Progress>
}