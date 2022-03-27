package strings

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import settings.AppSettings

internal class ENStringGetterTest {

    @Test
    fun test_serialize_to_json() {
        val json = Json.encodeToString(ENStringGetter())
        println("serialized:")
        println(json)
    }
}