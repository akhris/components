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

    private val _settingsValue = MutableStateFlow(AppSettings(listOf()))

    val settingsValue: StateFlow<AppSettings> = _settingsValue

    private val currentUserPath = System.getProperty("user.home")       //"/home/user"
    private val componentsSupPath = ".components_app"
    private val appSettingsFileName = "app_settings.json"
    private val defaultComponentsDatabaseFilename = "components.db"
    private val appSettingsPath = Path(currentUserPath, componentsSupPath)
    private val appSettingsFile = Path(currentUserPath, componentsSupPath, appSettingsFileName)
    private val defaultDBLocation = Path(currentUserPath, componentsSupPath, defaultComponentsDatabaseFilename)


//    val isLightTheme: Flow<Boolean?> =
//        _settingsFlow
//            .map { (it?.settings?.find { s -> s.key == key_is_light_theme } as? AppSetting.BooleanSetting)?.value }
//            .distinctUntilChanged()
//
//    val dbLocation =
//        _settingsFlow
//            .map {
//                ((it?.settings?.find { s -> s.key == key_db_location } as? AppSetting.StringSetting)?.value)
//                    ?: defaultDBLocation.toString()
//            }
//            .distinctUntilChanged()

    fun setAppSetting(appSetting: AppSetting) {
        scope.launch {
            setSetting(appSetting)
        }
    }

    fun setIsLightTheme(isLight: Boolean) {
        scope.launch {
            setSetting(AppSetting.BooleanSetting(key_is_light_theme, isLight))
        }
    }

    fun setDBLocation(dbLocation: String) {
        scope.launch {
            setSetting(AppSetting.StringSetting(key_db_location, dbLocation))
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
            val settings = settingsText.toAppSettings()
            settings?.let {
                _settingsValue.value = it
            }
            log("loaded settings from file: $settings")
        } else {
            log("File $appSettingsFile does not exist, creating one:")
            val path = withContext(Dispatchers.IO) {
                appSettingsPath.createDirectories()
                appSettingsFile.createFile()
            }
            log("File $path created")
        }
    }

    init {
        scope.launch { invalidateSettings() }
    }


    companion object {
        const val key_is_light_theme = "key_is_light_theme"
        const val key_db_location = "key_db_location"
    }
}
