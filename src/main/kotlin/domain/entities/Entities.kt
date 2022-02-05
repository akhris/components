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
    val name: String,
    val parameters: List<Parameter>
) : IEntity<String>

/**
 * e.g. name = resistance
 */
data class Parameter(
    override val id: String,
    val name: String,
    val description: String
) : IEntity<String>

data class Value(
    override val id: String,
    val parameter: Parameter,
    val value: String,
    val unit: Unit
) : IEntity<String>

data class Unit(
    override val id: String,
    val unit: String
) : IEntity<String>
