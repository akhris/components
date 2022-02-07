package domain.entities

import com.akhris.domain.core.entities.IEntity

data class Item(
    override val id: String,
    val name: String,
    val type: ObjectType,
    val values: List<Value>
) : IEntity<String>

/**
 * e.g. name = resistors,
 * parameters = [resistance, tolerance, package]
 */
data class ObjectType(
    override val id: String,
    val name: String = "",
    val parameters: List<Parameter> = listOf()
) : IEntity<String>

/**
 * e.g. name = resistance
 */
data class Parameter(
    override val id: String,
    val name: String = "",
    val description: String = "",
    val unit: Unit? = null
) : IEntity<String>

data class Value(
    override val id: String,
    val parameter: Parameter,
    val value: String,
    val factor: Float = 1f
) : IEntity<String>

/**
 * e.g. m, cm, km
 */
data class Unit(
    override val id: String,
    val unit: String = "",
    val isMultipliable: Boolean = false
) : IEntity<String>
