package com.server.database.tables

import org.jetbrains.exposed.sql.Table

object UserTable: Table("UserTable") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50)
    val firstName = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val phoneNumberL = varchar("phone number", 30)
    val email = varchar("email", 100)
    val password = varchar("password", 50)
    val sex = varchar("sex", 30)
    val growth = integer("growth")
    val height = integer("height")
    override val primaryKey: PrimaryKey = PrimaryKey(email, name = "EMAIL")
}