package strings

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class ENStringGetterTest {

    @Test
    fun test_serialize_to_json() {
        val json = Json.encodeToString(ENStringDataSource())
        println("serialized:")
        println(json)
    }
}