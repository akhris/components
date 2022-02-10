package ui.screens.settings

import settings.AppSetting

interface ISetting {
    val setting: AppSetting
    val name: String
    val description: String?
    val iconPath: String?
}