package com.server.database.daoInterface

import com.server.models.Settings
import com.server.models.User

interface SettingsDAOInterface {
    fun createSettings(
        user: User,
        restTime: Int,
        countDownTime: Int
    )
    fun updateSettings(
        id: Int,
        user: User,
        restTime: Int,
        countDownTime: Int
    )
    fun deleteSettingsWithId(id: Int)
    fun deleteSettingsWithUser(user: User)
    fun deleteSettingsWithUserId(userId: Int)
    fun getSettingsWithUser(user: User) : Settings?
    fun getSettingsWithUserId(userId: Int) : Settings?
    fun getSettingsWithId(id: Int) : Settings?
}