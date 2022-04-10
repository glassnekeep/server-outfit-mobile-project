package com.server.database.dao

import com.server.database.daoInterface.*
import com.server.database.tables.*
import com.server.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class Dao(val db: Database) : BaseDaoInterface, UserDAOInterface, CalendarDAOInterface,
    ExerciseDAOInterface, ProgramDAOInterface, ProgressDAOInterface, SettingsDAOInterface {

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
        /*SchemaUtils.create(UserTable)
        SchemaUtils.create(CalendarTable)
        SchemaUtils.create(ExerciseTable)
        SchemaUtils.create(ProgramTable)
        SchemaUtils.create(ProgressTable)
        SchemaUtils.create(SettingsTable)
        SchemaUtils.create(ProgramToUserTable)
        SchemaUtils.create(ExerciseToProgramTable)*/
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

    override fun getAllUsers(): List<User> = transaction(db) {
        UserTable.selectAll().map {
            createUserWithRow(it)
        }
    }

    override fun getAllUsersId(): List<Int> = transaction(db) {
        UserTable.selectAll().map { it[UserTable.id] }
    }

    override fun addUserToProgram(programId: Int, userId: Int) = transaction(db) {
        ProgramToUserTable.insert {
            it[ProgramToUserTable.programId] = programId
            it[ProgramToUserTable.userId] = userId
        }
        Unit
    }

    override fun addExerciseToProgram(exerciseId: Int, programId: Int) = transaction(db) {
        ExerciseToProgramTable.insert {
            it[ExerciseToProgramTable.exerciseId] = exerciseId
            it[ExerciseToProgramTable.programId] = programId
        }
        Unit
    }

    override fun createProgram(name: String, interval: Int, exercises: List<Exercise>, users: List<User>, image: String) = transaction(db) {
        val id = ProgramTable.insertAndGetId {
            it[ProgramTable.name] = name
            it[ProgramTable.interval] = interval
            it[ProgramTable.image] = image
        }.value
        exercises.forEach { value ->
            addExerciseToProgram(value.id, id)
        }
        users.forEach { user ->
            addUserToProgram(id, user.id)
        }
    }

    override fun createEmptyProgram(interval: Int) {
        ProgramTable.insert {
            it[ProgramTable.interval] = interval
        }
    }

    override fun updateProgram(id: Int, name: String, interval: Int, exercises: List<Exercise>, users: List<User>, image: String) = transaction(db) {
        ProgramTable.update({ ProgramTable.id eq id }) {
            it[ProgramTable.name] = name
            it[ProgramTable.interval] = interval
            it[ProgramTable.image] = image
        }
        val currentExerciseList = getExerciseListWithProgramId(id)
        val currentUserList = getUserListWithProgramId(id)
        val listOfDeletedExercises = currentExerciseList.minus(exercises.toSet()).map{ it.id }
        val listOfNewExercises = exercises.minus(currentExerciseList.toSet()).map{ it.id }
        val listOfDeletedUsers = currentUserList.minus(users.toSet()).map{ it.id }
        val listOfNewUsers = users.minus(currentUserList.toSet()).map { it.id }
        ExerciseToProgramTable.deleteWhere { ExerciseToProgramTable.exerciseId inList listOfDeletedExercises }
        /*ExerciseToProgramTable.insert { newValue ->
            listOfNewExercises.forEach { exerciseId ->
                newValue[ExerciseToProgramTable.programId] = id
                newValue[ExerciseToProgramTable.exerciseId] = exerciseId
            }
        }*/
        ProgramToUserTable.deleteWhere { ProgramToUserTable.userId inList listOfDeletedUsers }
        listOfNewExercises.forEach { exerciseId ->
            addExerciseToProgram(exerciseId, id)
        }
        listOfNewUsers.forEach { userId ->
            addUserToProgram(id, userId)
        }
        Unit
    }

    override fun deleteProgram(id: Int) = transaction(db) {
        ProgramTable.deleteWhere { ProgramTable.id eq id }
        Unit
    }

    override fun getProgramWithId(id: Int) : Program? = transaction(db) {
        ProgramTable.select { ProgramTable.id eq id }.map {
            Program(
                it[ProgramTable.id].value,
                it[ProgramTable.name],
                it[ProgramTable.interval],
                getExerciseListWithProgramId(it[ProgramTable.id].value),
                getUserListWithProgramId(it[ProgramTable.id].value),
                it[ProgramTable.image]
            )
        }.singleOrNull()
    }

    override fun getProgramListWithUser(user: User): List<Program> = transaction(db) {
        ProgramTable.innerJoin(ProgramToUserTable).innerJoin(UserTable).slice(ProgramTable.columns).select { UserTable.id eq user.id }.map {
            createProgramWithRow(it)
        }
    }

    override fun getUserListWithProgram(program: Program): List<User> = transaction(db) {
        UserTable.innerJoin(ProgramToUserTable).innerJoin(ProgramTable).slice(UserTable.columns).select { ProgramTable.id eq program.id }.map {
            createUserWithRow(it)
        }
    }

    override fun getUserListWithProgramId(id: Int): List<User> = transaction(db) {
        UserTable.innerJoin(ProgramToUserTable).innerJoin(ProgramTable).slice(ProgramTable.columns).select { ProgramTable.id eq id }.map {
            createUserWithRow(it)
        }
    }

    override fun getProgramListWithUserId(userId: Int): List<Program> = transaction(db) {
        ProgramTable.innerJoin(ProgramToUserTable).innerJoin(UserTable).slice(ProgramTable.columns).select { UserTable.id eq userId }.map {
            createProgramWithRow(it)
        }
    }

    override fun getAllPrograms(): List<Program>  = transaction(db) {
        ProgramTable.selectAll().map {
            createProgramWithRow(it)
        }
    }

    private fun createCalendarWithRow(row: ResultRow) : Calendar {
        val program = getProgramWithId(row[CalendarTable.program])
        val user = getUserWithId(row[CalendarTable.user])
        return Calendar(
            row[CalendarTable.id].value,
            row[CalendarTable.date],
            program!!,
            user!!
        )
    }

    private fun createExerciseWithRow(row: ResultRow) : Exercise {
        return Exercise(
            row[ExerciseTable.id],
            row[ExerciseTable.name],
            row[ExerciseTable.time],
            row[ExerciseTable.numberOfApproaches],
            row[ExerciseTable.periods],
            row[ExerciseTable.weight],
            row[ExerciseTable.image]
        )
    }

    private fun createProgressWihRow(row: ResultRow) : Progress {
        return Progress(
            row[ProgressTable.id],
            getProgramWithId(row[ProgressTable.programId])!!,
            getUserWithId(row[ProgressTable.userId])!!,
            row[ProgressTable.currentExercise]
        )
    }

    private fun createSettingsWithRow(row: ResultRow) : Settings {
        return Settings(
            row[SettingsTable.id],
            getUserWithId(row[SettingsTable.userId])!!,
            row[SettingsTable.restTime],
            row[SettingsTable.countDownTime]
        )
    }

    private fun createProgramWithRow(row: ResultRow) : Program {
        val programId = row[ProgramTable.id].value
        return Program(
            programId,
            row[ProgramTable.name],
            row[ProgramTable.interval],
            getExerciseListWithProgramId(programId),
            getUserListWithProgramId(programId),
            row[ProgramTable.image]
        )
    }

    override fun createCalendar(date: String, program: Program, user: User) = transaction(db) {
        CalendarTable.insert {
            it[CalendarTable.date] = date
            it[CalendarTable.program] = program.id
            it[CalendarTable.user] = user.id
        }
        Unit
    }

    override fun updateCalendar(id: Int, date: String, program: Program, user: User) = transaction(db) {
        CalendarTable.update({ CalendarTable.id eq id }) {
            it[CalendarTable.date] = date
            it[CalendarTable.program] = program.id
            it[CalendarTable.user] = user.id
        }
        Unit
    }

    override fun deleteCalendar(id: Int) = transaction(db) {
        CalendarTable.deleteWhere {
            CalendarTable.id eq id
        }
        Unit
    }

    override fun getCalendarWithId(id: Int): Calendar? = transaction(db) {
        CalendarTable.select { CalendarTable.id eq id }.map {
            createCalendarWithRow(it)
        }.singleOrNull()
    }

    override fun getCalendarListWithUser(userId: Int): List<Calendar> = transaction(db) {
        CalendarTable.select { CalendarTable.user eq userId }.map {
            createCalendarWithRow(it)
        }
    }

    override fun createExercise(
        name: String,
        time: Int,
        numberOfApproaches: Int,
        periods: Int,
        weight: Int,
        image: String
    ) = transaction(db) {
        ExerciseTable.insert {
            it[ExerciseTable.name] = name
            it[ExerciseTable.time] = time
            it[ExerciseTable.numberOfApproaches] = numberOfApproaches
            it[ExerciseTable.periods] = periods
            it[ExerciseTable.weight] = weight
            it[ExerciseTable.image] = image
        }
        Unit
    }

    override fun updateExercise(
        id: Int,
        name: String,
        time: Int,
        numberOfApproaches: Int,
        periods: Int,
        weight: Int,
        image: String
    ) = transaction(db) {
        ExerciseTable.update({ ExerciseTable.id eq id }) {
            it[ExerciseTable.id] = id
            it[ExerciseTable.name] = name
            it[ExerciseTable.time] = time
            it[ExerciseTable.numberOfApproaches] = numberOfApproaches
            it[ExerciseTable.periods] = periods
            it[ExerciseTable.weight] = weight
            it[ExerciseTable.image] = image
        }
        Unit
    }

    override fun deleteExercise(id: Int) = transaction(db) {
        ExerciseTable.deleteWhere { ExerciseTable.id eq id }
        Unit
    }

    override fun getExercise(id: Int): Exercise? = transaction(db) {
        ExerciseTable.select { ExerciseTable.id eq id }.map {
            createExerciseWithRow(it)
        }.singleOrNull()
    }

    override fun getExerciseListWIthProgram(program: Program): List<Exercise> = transaction(db) {
        ExerciseTable.innerJoin(ExerciseToProgramTable).innerJoin(ProgramTable).slice(ProgramTable.columns).select {
            ProgramTable.id eq program.id
        }.map {
            createExerciseWithRow(it)
        }
        //TODO Тут и в других местах надо обработать ошибки при получении из бд null
    }

    override fun getExerciseListWithProgramId(id: Int): List<Exercise> = transaction(db) {
        ExerciseTable.innerJoin(ExerciseToProgramTable).innerJoin(ProgramTable).slice(ExerciseTable.columns).select {
            ExerciseTable.id eq id
        }.map {
            createExerciseWithRow(it)
        }
    }

    override fun createProgress(program: Program, user: User, currentExercise: Int) = transaction(db) {
        ProgressTable.insert {
            val programId = program.id
            val userId = user.id
            it[ProgressTable.programId] = programId
            it[ProgressTable.userId] = userId
            it[ProgressTable.currentExercise] = currentExercise
        }
        Unit
    }

    override fun updateProgress(id: Int, program: Program, user: User, currentExercise: Int) = transaction(db) {
        ProgressTable.update ({ ProgressTable.id eq id }) {
            val programId = program.id
            val userId = user.id
            it[ProgressTable.programId] = programId
            it[ProgressTable.userId] = userId
            it[ProgressTable.currentExercise] = currentExercise
        }
        Unit
    }

    override fun deleteProgress(id: Int) = transaction(db) {
        ProgressTable.deleteWhere { ProgressTable.id eq id }
        Unit
    }

    override fun getProgressWithId(id: Int): Progress? = transaction(db) {
        ProgressTable.select { ProgressTable.id eq id }.map {
            createProgressWihRow(it)
        }.singleOrNull()
    }

    override fun getProgressWithUserAndProgram(user: User, program: Program): Progress? = transaction(db) {
        ProgressTable.select { (ProgressTable.userId eq user.id) and (ProgressTable.programId eq program.id) }.map {
            createProgressWihRow(it)
        }.singleOrNull()
    }

    override fun getProgressWithUserIdAndProgramId(userId: Int, programId: Int): Progress? = transaction(db) {
        ProgressTable.select { (ProgressTable.userId eq userId) and (ProgressTable.programId eq programId) }.map {
            createProgressWihRow(it)
        }.singleOrNull()
    }

    override fun createSettings(user: User, restTime: Int, countDownTime: Int) = transaction(db) {
        SettingsTable.insert {
            val userId = user.id
            it[SettingsTable.userId] = userId
            it[SettingsTable.restTime] = restTime
            it[SettingsTable.countDownTime] = countDownTime
        }
        Unit
    }

    override fun updateSettings(id: Int, user: User, restTime: Int, countDownTime: Int) = transaction(db) {
        SettingsTable.update({ SettingsTable.id eq id }) {
            it[SettingsTable.userId] = user.id
            it[SettingsTable.restTime] = restTime
            it[SettingsTable.countDownTime] = countDownTime
        }
        Unit
    }

    override fun deleteSettingsWithId(id: Int) = transaction(db) {
        SettingsTable.deleteWhere { SettingsTable.id eq id }
        Unit
    }

    override fun deleteSettingsWithUser(user: User) = transaction(db) {
        SettingsTable.deleteWhere { SettingsTable.userId eq user.id }
        Unit
    }

    override fun deleteSettingsWithUserId(userId: Int) = transaction(db) {
        SettingsTable.deleteWhere { SettingsTable.userId eq userId }
        Unit
    }

    override fun getSettingsWithUser(user: User): Settings? = transaction(db) {
        SettingsTable.select { SettingsTable.userId eq user.id }.map {
            createSettingsWithRow(it)
        }.singleOrNull()
    }

    override fun getSettingsWithUserId(userId: Int): Settings? = transaction(db) {
        SettingsTable.select { SettingsTable.userId eq userId }.map {
            createSettingsWithRow(it)
        }.singleOrNull()
    }

    override fun getSettingsWithId(id: Int): Settings? = transaction(db) {
        SettingsTable.select { SettingsTable.id eq id }.map {
            createSettingsWithRow(it)
        }.singleOrNull()
    }
}