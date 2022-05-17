package settings

import com.akhris.domain.core.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import strings.ENStringGetter
import strings.StringProvider
import strings.toLocalStringGetter
import utils.FileUtils
import utils.replace
import java.nio.file.Files
import kotlin.io.path.*

class AppSettingsRepository(private val scope: CoroutineScope) {


    private val _settingsValue = MutableStateFlow(AppSettings.default)

    val settingsValue: StateFlow<AppSettings> = _settingsValue

    private val appSettingsFileName = "app_settings.json"
    private val appSettingsFile = AppFoldersManager.getAppPath().resolve(appSettingsFileName)


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


    fun getLocalizedStringProvider(): Flow<StringProvider> {
        return _settingsValue
            .mapNotNull {
                it.settings.find { s -> s.key == key_localization_file } as? AppSetting.ListSetting
            }
            .distinctUntilChanged()
            .map {
                try {
                    val jsonString = Path(it.selectedKey).readText()
                    jsonString.toLocalStringGetter()
                } catch (e: Exception) {
                    log("exception while deserializing: $e")
                    null
                } ?: ENStringGetter()
            }.map {
                StringProvider(it)
            }
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


    fun readAppSettingsFromFile(): AppSettings? {
        if (appSettingsFile.notExists()) {
            return null
        }
        return appSettingsFile.readText().toAppSettings()
    }

    private suspend fun invalidateSettings() {
        log("invalidate settings")
        if (appSettingsFile.exists()) {
            log("File $appSettingsFile exists")
            val settingsFromFile = withContext(Dispatchers.IO) {
                readAppSettingsFromFile()
            }
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
                            is AppSetting.ListSetting -> {
                                (currentSetting as? AppSetting.ListSetting)?.copy(
                                    selectedKey = setting.selectedKey,
                                    values = setting.values
                                )
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

    /**
     * Update list of files paths inside 'translations' directory
     */
    private suspend fun invalidateTranslations() {
        withContext(Dispatchers.IO) {
            val translationsFiles = Files.list(AppFoldersManager.translationsPath).toList()
            val translations = translationsFiles.mapNotNull { path ->
                path.readText().toLocalStringGetter()?.let { path.absolutePathString() to it.language }
            }.toMap()

            val defaultTranslation =
                mapOf("default" to ENStringGetter().language)

            log("translations found: ${translations.size}")
            translations.forEach { (t, u) ->
                log("$t: $u")
            }

            val translationsWithDefault = defaultTranslation.plus(translations)


            val translationSetting =
                (_settingsValue.value.settings.find { it.key == key_localization_file } as? AppSetting.ListSetting)?.copy(
                    values = translationsWithDefault
                )

            log("existed translationSetting: $translationSetting")

            setSetting(
                translationSetting ?: AppSetting.ListSetting(
                    key = key_localization_file,
                    values = translationsWithDefault,
                    selectedKey = translations.keys.firstOrNull() ?: ""
                )
            )

        }
    }

    init {
        scope.launch {
            invalidateSettings()
            invalidateTranslations()
        }
    }


    companion object {
        const val key_is_dark_theme = "key_is_dark_theme"
        const val key_db_location = "key_db_location"
        const val key_localization_file = "key_localization_file"

        const val defaultComponentsDatabaseFilename = "components.db"
    }
}
