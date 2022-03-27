package strings

import com.akhris.domain.core.utils.log
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import settings.AppSettings

@Serializable
open class LocalStringGetter(override val language: String, val translations: Map<String, String>) : IStringGetter {
    override fun getString(id: String): String? = translations[id]
}

fun LocalStringGetter.toJson(): String = Json.encodeToString(this)

fun String.toLocalStringGetter(): LocalStringGetter? = try {
    Json.decodeFromString<LocalStringGetter>(this)
} catch (e: SerializationException) {
    log("Cannot decode AppSettings from $this")
    null
}