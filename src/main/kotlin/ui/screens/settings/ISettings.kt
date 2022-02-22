package ui.screens.settings

import kotlinx.coroutines.flow.Flow
import settings.AppSetting

interface ISettings {

    val state: Flow<Model>

    fun onSettingClicked()

    fun onSettingChanged(newSetting: AppSetting)

    data class Model(
        val settings: List<ISetting>
    )
}