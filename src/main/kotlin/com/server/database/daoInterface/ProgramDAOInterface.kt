package com.server.database.daoInterface

import com.server.models.Program

interface ProgramDAOInterface {
    fun createProgramInterface(program: Program)
    fun updateProgramInterface(program: Program)
    fun deleteProgramInterface(program: Program)
}