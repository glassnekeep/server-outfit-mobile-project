package com.server.database.daoInterface

import com.server.models.Settings

interface SettingsDAOInterface {
    fun createSettings(settings: Settings)
    fun updateSettings(settings: Settings)
    fun deleteSettings(settings: Settings)
}