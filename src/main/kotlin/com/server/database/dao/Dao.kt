package com.server.database.dao

import com.server.database.daoInterface.BaseDaoInterface
import com.server.database.daoInterface.CalendarDAOInterface
import com.server.database.daoInterface.UserDAOInterface
import com.server.database.tables.*
import com.server.models.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class Dao(val db: Database): BaseDaoInterface {
    override fun init() = transaction(db) {
        SchemaUtils.create(tables = arrayOf(
            UserTable,
            CalendarTable,
            ExerciseTable,
            ProgramTable,
            ProgressTable,
            SettingsTable,
            ProgramToUserTable,
            ExerciseToProgramTable
        ))
    }

    override fun close() {
        
    }

}