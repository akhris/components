package domain.entities

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.IDUtils
import java.time.LocalDateTime

data class Item(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val type: ObjectType? = null,
    val values: List<Value> = listOf()
) : IEntity<String>

/**
 * e.g. name = resistors,
 * parameters = [resistance, tolerance, package]
 */
data class ObjectType(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val parameters: List<Parameter> = listOf()
) : IEntity<String>{
    override fun toString(): String = name
}

/**
 * e.g. name = resistance
 */
data class Parameter(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val unit: Unit? = null
) : IEntity<String> {
    override fun toString(): String = name
}

data class Value(
    override val id: String = IDUtils.newID(),
    val parameter: Parameter,
    val value: String,
    val factor: Float? = null
) : IEntity<String> {
    override fun toString(): String = value
}

/**
 * e.g. m, cm, km
 */
data class Unit(
    override val id: String = IDUtils.newID(),
    val unit: String = "",
    val isMultipliable: Boolean = false
) : IEntity<String> {
    override fun toString(): String = unit
}

/**
 * Box #1, Box #2, Room 407
 */
data class Container(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val parentContainer: Container? = null
) : IEntity<String> {
    override fun toString(): String = name
}


data class ItemIncome(
    override val id: String = IDUtils.newID(),
    val item: Item? = null,
    val container: Container? = null,
    val quantity: Long = 0L,
    val dateTime: LocalDateTime?,
    val supplier: Supplier? = null
) : IEntity<String>

data class ItemOutcome(
    override val id: String = IDUtils.newID(),
    val item: Item? = null,
    val container: Container? = null,
    val quantity: Long = 0L,
    val dateTime: LocalDateTime
) : IEntity<String>


data class Supplier(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val url: String = "",
    val isFavorite: Boolean = false
) : IEntity<String>

data class Project(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val items: List<Pair<Item, Long>> = listOf()
) : IEntity<String>