package strings

import com.akhris.domain.core.utils.log
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
open class LocalStringDataSource(override val language: String, val translations: Map<String, String>) : IStringDataSource {
    override fun getString(id: String): String? = translations[id]
}

fun LocalStringDataSource.toJson(): String = Json.encodeToString(this)

fun String.toLocalStringGetter(): LocalStringDataSource? = try {
    Json.decodeFromString<LocalStringDataSource>(this)
} catch (e: SerializationException) {
    log("Cannot decode AppSettings from $this")
    null
}