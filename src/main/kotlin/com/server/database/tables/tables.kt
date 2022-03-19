package com.server.database.tables
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.time

object Program: Table("programs") {
    val id = integer("id").autoIncrement()
    val interval = integer("interval")
    //TODO Тут скорее всего нужно определитьь связь между програм и пользователями/упражнениями через 2 промежуточные таблицы, для чего нужно определить первичные ключи
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object Exercise: Table("exercises") {
    val id = integer("id").autoIncrement()
    val time = time("time")
    val numberOfApproaches = integer("number_of_approaches")
    val periods = integer("periods")
    val weight = integer("weight")
    //TODO тут может быть как стринга - ссылка на картинку в интернете, так и сама картинка как ресурс
    val image = blob("image")
}

object Progress: Table("progress") {
    val id = integer("id").autoIncrement()
    val programId = integer("program_id")
    val userId = integer("user_id")
    val currentExercise = integer("current_exercise")
}

object Settings: Table("settings") {
    val id = integer("id").autoIncrement()
    val userId = integer("userId").uniqueIndex()
    val restTime = integer("rest_tIme")
    val countDownTime = integer("count_down_time")
    override val primaryKey: PrimaryKey = PrimaryKey(userId, name = "ID")
}

object Calendar: Table("calendar") {
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
    val firstName = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val phoneNumberL = varchar("phone_number", 30)
    //TODO Разобратьяс как быть с ункиальным индексами
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 50)
    val sex = varchar("sex", 30)
    val growth = integer("growth")
    val height = integer("height")
    override val primaryKey: PrimaryKey = PrimaryKey(email, name = "EMAIL")
}