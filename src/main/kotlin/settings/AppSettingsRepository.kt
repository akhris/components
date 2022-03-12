package settings

import com.akhris.domain.core.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.FileUtils
import utils.replace
import kotlin.io.path.*

class AppSettingsRepository(private val scope: CoroutineScope) {


    private val _settingsValue = MutableStateFlow(AppSettings.default)

    val settingsValue: StateFlow<AppSettings> = _settingsValue
    val appSettingsFileName = "app_settings.json"
    private val appSettingsFile = AppFoldersManager.getAppPath().resolve(appSettingsFileName)
//        Path(currentUserPath, componentsSupPath, appSettingsFileName)
//    private val defaultDBLocation = Path(currentUserPath, componentsSupPath, defaultComponentsDatabaseFilename)


    fun setAppSetting(appSetting: AppSetting) {
        scope.launch {
            setSetting(appSetting)
        }
    }

    fun observeSetting(key: String): Flow<AppSetting?> {
        return _settingsValue
            .map {
                it.settings.find { s -> s.key == key }
            }
    }

    fun getSetting(key: String): AppSetting? {
        return _settingsValue.value.settings.find { it.key == key }
    }

    private suspend fun setSetting(appSetting: AppSetting) {
        val currentSettings = _settingsValue.value ?: AppSettings(listOf())
        val currentSetting = currentSettings.settings.find { it.key == appSetting.key }
        val changedSettings = if (currentSetting == null) {
            //there is no such setting
            currentSettings.settings.plus(appSetting)
        } else {
            //there is setting with such key
            currentSettings.settings.replace(appSetting) {
                it.key == appSetting.key
            }
        }
        //write changed settings to file:
        FileUtils.writeText(appSettingsFile.toString(), currentSettings.copy(settings = changedSettings).toJson())

        //invalidate settings from file:
        invalidateSettings()
    }

    private suspend fun invalidateSettings() {
        log("invalidate settings")
        if (appSettingsFile.exists()) {
            log("File $appSettingsFile exists")
            val settingsText = withContext(Dispatchers.IO) {
                appSettingsFile.readText()
            }
            val settingsFromFile = settingsText.toAppSettings()
            settingsFromFile?.let {
                //change _settingsValue settings to settings loaded from file by key:
                _settingsValue.value = _settingsValue.value.copy(
                    settings = _settingsValue.value.settings.map { currentSetting ->
                        when (val setting = settingsFromFile.settings.find { sff -> sff.key == currentSetting.key }) {
                            is AppSetting.BooleanSetting -> {
                                (currentSetting as? AppSetting.BooleanSetting)?.copy(value = setting.value)
                            }
                            is AppSetting.FloatSetting -> {
                                (currentSetting as? AppSetting.FloatSetting)?.copy(value = setting.value)
                            }
                            is AppSetting.PathSetting -> {
                                (currentSetting as? AppSetting.PathSetting)?.copy(value = setting.value)
                            }
                            is AppSetting.StringSetting -> {
                                (currentSetting as? AppSetting.StringSetting)?.copy(value = setting.value)
                            }
                            null -> null
                        } ?: currentSetting

                    }
                )
            }
            log("loaded settings from file: $settingsFromFile")
        } else {
            log("File $appSettingsFile does not exist, creating one:")
            val path = withContext(Dispatchers.IO) {
                appSettingsFile.createFile()
            }
            log("File $path created")
        }
    }

    init {
        scope.launch { invalidateSettings() }
    }


    companion object {
        const val key_is_dark_theme = "key_is_dark_theme"
        const val key_db_location = "key_db_location"


        const val defaultComponentsDatabaseFilename = "components.db"
    }
}
