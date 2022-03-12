package settings

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.akhris.domain.core.utils.log
import kotlin.io.path.pathString

@Serializable
data class AppSettings(val settings: List<AppSetting>) {

    companion object {
        val default = AppSettings(
            listOf(
                AppSetting.BooleanSetting(AppSettingsRepository.key_is_dark_theme, true),
                AppSetting.PathSetting(
                    AppSettingsRepository.key_db_location,
                    AppFoldersManager.getAppPath()
                        .resolve(AppSettingsRepository.defaultComponentsDatabaseFilename).pathString
                )
            )
        )
    }
}

@Serializable
sealed class AppSetting {
    abstract val key: String

    @Serializable
    data class FloatSetting(override val key: String, val value: Float) : AppSetting()

    @Serializable
    data class StringSetting(override val key: String, val value: String) : AppSetting()

    @Serializable
    data class PathSetting(override val key: String, val value: String) : AppSetting()

    @Serializable
    data class BooleanSetting(override val key: String, val value: Boolean) : AppSetting()
}

fun AppSettings.toJson(): String = Json.encodeToString(this)

fun String.toAppSettings(): AppSettings? = try {
    Json.decodeFromString<AppSettings>(this)
} catch (e: SerializationException) {
    log("Cannot decode AppSettings from $this")
    null
}