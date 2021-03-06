package com.server.database.tables
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

//TODO Поработать тут с blob и date, потому что после того как я все зависимости привел в порядок, должно получиться использовать их, а не стринги кругом
object ProgramTable: IntIdTable("programs") {
    //val id = integer("id").autoIncrement()
    val interval = integer("interval")
    val name = text("name")
    val image = text("image")
    val numberOfExercises = integer("number_of_exercises")
    //TODO Тут скорее всего нужно определить связь между программой и пользователями/упражнениями через 2 промежуточные таблицы, для чего нужно определить первичные ключи
    //override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object ExerciseTable: Table("exercises") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val time = integer("time")
    val numberOfApproaches = integer("number_of_approaches")
    val periods = integer("periods")
    val weight = integer("weight")
    //TODO тут может быть как стринга - ссылка на картинку в интернете, так и сама картинка как ресурс
    val image = text("image")
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
    val restTime = integer("rest_time")
    val countDownTime = integer("count_down_time")
    //override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object CalendarTable: IntIdTable("calendar") {
    //val id = integer("id").autoIncrement()
    val date = text("date")
    //val exercise = integer("exercise_id")
    val program = integer("program_id")
    val user = integer("user_id")
   //override val primaryKey: PrimaryKey = PrimaryKey(id, name = "ID")
}

object UserTable: IntIdTable("users") {
    //val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val firstname = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val phoneNumber = varchar("phone_number", 30).uniqueIndex()
    //TODO Разобраться как быть с уникальными индексами
    val email = varchar("email", 100).uniqueIndex()
    val password = text("password")
    val sex = varchar("sex", 30)
    val growth = integer("growth")
    val weight = integer("height")
    //override val primaryKey: PrimaryKey = PrimaryKey(id, name = "EMAIL")
}

object ExerciseToProgramTable: Table("exercise_to_program") {
    val exerciseId = reference("exercise_id", ExerciseTable.id, onDelete = ReferenceOption.CASCADE)
    val programId = reference("program_id", ProgramTable.id, onDelete = ReferenceOption.CASCADE)
}

object ProgramToUserTable: Table("program_to_user") {
    val programId = reference("program_id", ProgramTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = reference("user_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
}

object SharedProgressTable: Table("shared_progress_table") {
    val senderId = reference("sender_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val recipientId = reference("recipient_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val time = text("time")
}