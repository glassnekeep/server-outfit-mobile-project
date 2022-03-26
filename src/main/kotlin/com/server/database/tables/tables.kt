package com.server.database.tables
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time

object ProgramTable: Table("programs") {
    val id = integer("id").autoIncrement()
    val interval = integer("interval")
    //TODO Тут скорее всего нужно определить связь между программой и пользователями/упражнениями через 2 промежуточные таблицы, для чего нужно определить первичные ключи
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object ExerciseTable: Table("exercises") {
    val id = integer("id").autoIncrement()
    val time = time("time")
    val numberOfApproaches = integer("number_of_approaches")
    val periods = integer("periods")
    val weight = integer("weight")
    //TODO тут может быть как стринга - ссылка на картинку в интернете, так и сама картинка как ресурс
    val image = blob("image")
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object ProgressTable: Table("progress") {
    val id = integer("id").autoIncrement()
    val programId = integer("program_id")
    val userId = integer("user_id")
    val currentExercise = integer("current_exercise")
}

object SettingsTable: Table("settings") {
    val id = integer("id").autoIncrement()
    val userId = integer("userId").uniqueIndex()
    val restTime = integer("rest_tIme")
    val countDownTime = integer("count_down_time")
    override val primaryKey: PrimaryKey = PrimaryKey(userId, name = "ID")
}

object CalendarTable: Table("calendar") {
    val id = integer("id").autoIncrement()
    val date = date("date")
    val exercise = integer("exercise_id")
    val program = integer("program_id")
    val user = integer("user_id")
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50)
    val firstname = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val phoneNumber = varchar("phone_number", 30).uniqueIndex()
    //TODO Разобраться как быть с уникальными индексами
    val email = varchar("email", 100).uniqueIndex()
    val password = text("password")
    val sex = varchar("sex", 30)
    val growth = integer("growth")
    val weight = integer("height")
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "EMAIL")
}

object ExerciseToProgramTable: Table("exercise_to_program") {
    val exerciseId = reference("exercise_id", ExerciseTable.id)
    val programId = reference("program_id", ProgramTable.id)
}

object ProgramToUserTable: Table("program_to_user") {
    val programId = reference("program_id", ProgramTable.id)
    val userId = reference("user_id", UserTable.id)
}