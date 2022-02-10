package ui.screens.settings

import settings.AppSetting
import settings.AppSettingsRepository

class SettingsMapper {
    fun map(setting: AppSetting): ISetting {
        return object : ISetting {
            override val setting: AppSetting = setting
            override val name: String = when (setting.key) {
                AppSettingsRepository.key_is_light_theme -> "Select mode"
                AppSettingsRepository.key_db_location -> "database location"
                else -> ""
            }

            override val description: String? = when (setting.key) {
                AppSettingsRepository.key_is_light_theme -> "Toggle theme colors to light/dark mode"
                else -> null
            }
            override val iconPath: String? = when (setting.key) {
                AppSettingsRepository.key_is_light_theme -> "vector/dark_mode_black_24dp.svg"
                else -> null
            }

        }
    }
}