package persistence.exposed

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object Tables {
    object Units : UUIDTable() {
        val unit: Column<String> = text(name = "unit")
        val isMultipliable: Column<Boolean> = bool(name = "isMultipliable")
    }
}