package ui.screens.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import settings.AppSetting
import settings.AppSettingsRepository


class SettingsComponent(
    componentContext: ComponentContext,
    mapper: SettingsMapper = SettingsMapper(),
    private val appSettingsRepository: AppSettingsRepository
) : ISettings,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val state: Flow<ISettings.Model> =
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

    init{
        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })

        //update

    }



}