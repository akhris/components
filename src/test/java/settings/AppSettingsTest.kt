package settings

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class AppSettingsTest {
    lateinit var settings: AppSettings

    lateinit var settingsJSON: String

    @BeforeEach
    fun setUp() {
        settings = AppSettings(
            listOf(
                AppSetting.BooleanSetting("is_light_theme", false),
                AppSetting.FloatSetting("factor", 0.43432f),
                AppSetting.StringSetting("database_path", """c:\users\database.db""")
            )
        )

        settingsJSON = """
            {"settings":[{"type":"settings.AppSetting.BooleanSetting","key":"is_light_theme","value":false},{"type":"settings.AppSetting.FloatSetting","key":"factor","value":0.43432},{"type":"settings.AppSetting.StringSetting","key":"database_path","value":"c:\\users\\database.db"}]}
        """.trimIndent()
    }

    @Test
    fun test_serialize_to_json() {
        val json = Json.encodeToString(settings)
        println("serialized:")
        println(json)
        assert(json == settingsJSON)
    }

    @Test
    fun test_deserialize_from_json() {
        val deserialized = Json.decodeFromString<AppSettings>(settingsJSON)
        println("deserialized:")
        println(deserialized)
        assert(settings == deserialized)
    }
}