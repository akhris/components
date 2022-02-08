package domain.entities

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.IDUtils

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
) : IEntity<String>

/**
 * e.g. name = resistance
 */
data class Parameter(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val unit: Unit? = null
) : IEntity<String>

data class Value(
    override val id: String = IDUtils.newID(),
    val parameter: Parameter,
    val value: String,
    val factor: Float? = null
) : IEntity<String>

/**
 * e.g. m, cm, km
 */
data class Unit(
    override val id: String = IDUtils.newID(),
    val unit: String = "",
    val isMultipliable: Boolean = false
) : IEntity<String>

/**
 * Box #1, Box #2, Room 407
 */
data class Container(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val parentContainer: Container? = null
) : IEntity<String>