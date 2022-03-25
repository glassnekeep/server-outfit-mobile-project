package com.server.database.daoInterface

import com.server.models.*

interface ProgressDAOInterface {
    fun createProgress(progress: Progress)
    fun updateProgressInterface(progress: Progress)
    fun deleteProgress(progress: Progress)
}