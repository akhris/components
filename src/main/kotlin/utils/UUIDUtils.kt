package utils

import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)

fun String.toUUIDorNull():UUID? = try {
    UUID.fromString(this)
} catch (e: IllegalArgumentException) {
    null
}
