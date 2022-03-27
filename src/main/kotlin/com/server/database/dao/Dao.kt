package com.server.database.dao

import com.server.database.daoInterface.*
import com.server.database.tables.*
import com.server.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction

class Dao(val db: Database) : BaseDaoInterface, UserDAOInterface, CalendarDAOInterface,
    ExerciseDAOInterface, ProgramDAOInterface, ProgressDAOInterface, SettingsDAOInterface {

    override fun init() {
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

    private fun createUserWithRow(row: ResultRow) : User {
        return User(
            row[UserTable.id],
            row[UserTable.username],
            row[UserTable.firstname],
            row[UserTable.lastname],
            row[UserTable.phoneNumber],
            row[UserTable.email],
            row[UserTable.password],
            row[UserTable.sex],
            row[UserTable.growth],
            row[UserTable.weight]
        )
    }

    override fun createUser(
        username: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        email: String,
        password: String,
        sex: String,
        growth: Int,
        weight: Int
    ) = transaction(db) {
        UserTable.insert {
            it[UserTable.username] = username
            it[UserTable.firstname] = firstname
            it[UserTable.lastname] = lastname
            it[UserTable.phoneNumber] = phoneNumber
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.sex] = sex
            it[UserTable.growth] = growth
            it[UserTable.weight] = weight
        }
        Unit
    }

    override fun updateUserWithId(
        id: Int,
        username: String,
        firstname: String,
        lastname: String,
        phoneNumber: String,
        email: String,
        password: String,
        sex: String,
        growth: Int,
        weight: Int
    ) = transaction(db) {
        UserTable.update({ UserTable.id eq id }) {
            it[UserTable.username] = username
            it[UserTable.firstname] = firstname
            it[UserTable.lastname] = lastname
            it[UserTable.phoneNumber] = phoneNumber
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.sex] = sex
            it[UserTable.growth] = growth
            it[UserTable.weight] = weight
        }
        Unit
    }

    override fun deleteUserWithId(id: Int) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.id eq id
        }
        Unit
    }

    override fun deleterUserWithEmail(email: String) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.email eq email
        }
        Unit
    }

    override fun deleterUserWithUsername(username: String) = transaction(db) {
        UserTable.deleteWhere {
            UserTable.username eq username
        }
        Unit
    }

    override fun deleteUserWithPhoneNumber(phoneNumber: String) {
        UserTable.deleteWhere {
            UserTable.phoneNumber eq phoneNumber
        }
        Unit
    }

    override fun getUserWithId(id: Int): User? = transaction(db) {
        UserTable.select { UserTable.id eq id }.map {
            createUserWithRow(it)
        }.singleOrNull()
    }

    override fun getUserWithEmail(email: String): User? = transaction(db) {
        UserTable.select { UserTable.email eq email }.map {
            createUserWithRow(it)
        }.singleOrNull()
    }

    override fun getUserWithUsername(username: String): User? = transaction(db) {
        UserTable.select { UserTable.username eq username }.map {
            createUserWithRow(it)
        }.singleOrNull()
    }

    override fun getUserWithPhoneNumber(phoneNumber: String): User? = transaction(db) {
        UserTable.select { UserTable.phoneNumber eq phoneNumber }.map {
            createUserWithRow(it)
        }.singleOrNull()
    }

    override fun createProgram(interval: Int, exercise: List<Exercise>, users: List<User>) = transaction(db) {
        ProgramTable.insert {
            it[ProgramTable.interval] = interval
        }
        Unit
    }

    override fun updateProgram(id: Int, interval: Int, exercise: List<Exercise>, users: List<User>) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun deleteProgram(id: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getProgramWithId(id: Int) : Program = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getProgramListWithUser(user: User): List<Program> = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getUserListWithProgram(program: Program): List<User> = transaction(db) {
        TODO("Not yet implemented")
    }

    private fun createCalendarWithRow(row: ResultRow) : Calendar = transaction(db) {
        val program = getProgramWithId(row[CalendarTable.program])
        val user = getUserWithId(row[CalendarTable.user])
        return Calendar(
            row[CalendarTable.id],
            row[CalendarTable.date],
            program,
            user!!
        )
    }

    override fun createCalendar(date: String program: Program, user: User) = transaction(db) {
        CalendarTable.insert {
            it[CalendarTable.date] = date
            it[CalendarTable.program] = program.id
            it[CalendarTable.user] = user.id
        }
        Unit
    }

    override fun updateCalendar(id: Int, date: String, program: Program, user: User) = transaction(db) {
        CalendarTable.update({ CalendarTable.id eq id }) {
            it[CalendarTable.date] = date(date)
            it[CalendarTable.program] = program.id
            it[CalendarTable.user] = user.id
        }
        Unit
    }

    override fun deleteCalendar(id: Int) = transaction(db) {
        CalendarTable.deleteWhere {
            CalendarTable.id eq id
        }
    }

    override fun getCalendarWithId(id: Int): Calendar = transaction(db) {
        CalendarTable.select { CalendarTable.id eq id } {

        }

    }

    override fun getCalendarListWithUser(userId: Int): List<Calendar> = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun createExercise(time: String, numberOfApproaches: Int, periods: Int, weight: Int, image: String) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun updateExercise(
        id: Int,
        time: String,
        numberOfApproaches: Int,
        periods: Int,
        weight: Int,
        image: String
    ) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun deleteExercise(id: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getExercise(id: Int): Exercise = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getExerciseListWIthProgram(program: Program): List<Exercise> = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun createProgress(program: Program, user: User, currentExercise: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun updateProgress(id: Int, program: Program, user: User, currentExercise: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun deleteProgress(id: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getProgressWithId(id: Int): Progress = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getProgressWithUserAndProgram(user: User, program: Program): Progress = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun createSettings(user: User, restTime: Int, countDownTime: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun updateSettings(id: Int, user: User, restTime: Int, countDownTime: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun deleteSettingsWithId(id: Int) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun deleteSettingsWithUser(user: User) = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getSettingsWithUser(user: User): Settings = transaction(db) {
        TODO("Not yet implemented")
    }

    override fun getSettingsWithId(id: Int): Settings = transaction(db) {
        TODO("Not yet implemented")
    }
}