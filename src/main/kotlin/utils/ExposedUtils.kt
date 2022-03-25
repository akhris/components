package utils

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.statements.UpdateBuilder

@Suppress("UNCHECKED_CAST")
operator fun <T, S : Comparable<S>, ID : EntityID<S>?, E : S?> UpdateBuilder<T>.set(column: Column<ID>, value: E) =
    // see https://github.com/JetBrains/Exposed/issues/1275
    set(column as Column<S?>, value)