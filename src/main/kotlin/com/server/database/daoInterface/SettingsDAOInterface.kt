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
    fun getSettingsWithUser(user: User) : Settings
    fun getSettingsWithId(id: Int) : Settings
}