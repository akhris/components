package settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.FileUtils
import utils.replace
import kotlin.io.path.*

class AppSettingsRepository(private val scope: CoroutineScope) {

    private val _settingsFlow = MutableStateFlow<AppSettings?>(null)


    private val currentUserPath = System.getProperty("user.home")       //"/home/user"
    private val componentsSupPath = ".components_app"
    private val appSettingsFileName = "app_settings.json"
    private val defaultComponentsDatabaseFilename = "components.db"
    private val appSettingsPath = Path(currentUserPath, componentsSupPath)
    private val appSettingsFile = Path(currentUserPath, componentsSupPath, appSettingsFileName)
    private val defaultDBLocation = Path(currentUserPath, componentsSupPath, defaultComponentsDatabaseFilename)


    val isLightTheme: Flow<Boolean?> =
        _settingsFlow
            .map { (it?.settings?.find { s -> s.key == key_is_light_theme } as? AppSetting.BooleanSetting)?.value }
            .distinctUntilChanged()

    val dbLocation =
        _settingsFlow
            .map {
                ((it?.settings?.find { s -> s.key == key_db_location } as? AppSetting.StringSetting)?.value)
                    ?: defaultDBLocation.toString()
            }
            .distinctUntilChanged()

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


    private suspend fun setSetting(appSetting: AppSetting) {
        val currentSettings = _settingsFlow.value ?: AppSettings(listOf())
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
        println("invalidate settings")
        if (appSettingsFile.exists()) {
            println("File $appSettingsFile exists")
            val settingsText = withContext(Dispatchers.IO) {
                appSettingsFile.readText()
            }
            val settings = settingsText.toAppSettings()
            settings?.let {
                _settingsFlow.emit(it)
            }
            println("loaded settings from file: $settings")
        } else {
            println("File $appSettingsFile does not exist, creating one:")
            val path = withContext(Dispatchers.IO) {
                appSettingsPath.createDirectories()
                appSettingsFile.createFile()
            }
            println("File $path created")
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
