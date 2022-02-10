package ui.screens.settings

import com.arkivanov.decompose.value.Value
import settings.AppSetting

interface ISettings {

    val state: Value<Model>

    fun onSettingClicked()

    fun onSettingChanged(newSetting: AppSetting)

    data class Model(
        val settings: List<ISetting>
    )
}