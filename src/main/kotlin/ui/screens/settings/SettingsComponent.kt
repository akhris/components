package ui.screens.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import settings.AppSetting
import settings.AppSettingsRepository

class SettingsComponent(
    componentContext: ComponentContext,
    mapper: SettingsMapper = SettingsMapper(),
    private val appSettingsRepository: AppSettingsRepository
) : ISettings,
    ComponentContext by componentContext {


    override val state: Value<ISettings.Model> =
        appSettingsRepository
            .settingsValue
            .map { appsettings ->
                ISettings.Model(appsettings.settings.map { mapper.map(it) })
            }

    override fun onSettingClicked() {
        TODO("Not yet implemented")
    }

    override fun onSettingChanged(newSetting: AppSetting) {
        appSettingsRepository.setAppSetting(newSetting)
    }

}