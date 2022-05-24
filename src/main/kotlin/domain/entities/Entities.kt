package domain.entities

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.IDUtils
import com.akhris.domain.core.utils.log
import java.time.LocalDateTime

data class Item(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val type: ObjectType? = null,
    val values: List<EntityValuable<Parameter>> = listOf()
) : IEntity<String> {
    override fun toString(): String = name
}

/**
 * e.g. name = resistors,
 * parameters = [resistance, tolerance, package]
 */
data class ObjectType constructor(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val parameters: List<Parameter> = listOf(),
    override val parentEntity: ObjectType? = null
) : IParentableEntity<String, ObjectType> {
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
    val item: EntityCountable<Item>? = null,
    val container: Container? = null,
    val dateTime: LocalDateTime? = null,
    val supplier: Supplier? = null,
    val invoice: Invoice? = null
) : IEntity<String>

data class ItemOutcome(
    override val id: String = IDUtils.newID(),
    val item: EntityCountable<Item>? = null,
    val container: Container? = null,
    val dateTime: LocalDateTime? = null
) : IEntity<String>


data class WarehouseItem(
    override val id: String = IDUtils.newID(),
    val item: EntityCountable<Item>? = null,
    val container: Container? = null
) : IEntity<String>

data class Supplier(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val url: String = "",
    val isFavorite: Boolean = false
) : IEntity<String> {
    override fun toString(): String = name
}

data class Project(
    override val id: String = IDUtils.newID(),
    val name: String = "",
    val description: String = "",
    val dateTime: LocalDateTime? = null,
    val extFile: String? = null,
    val items: List<EntityCountable<Item>> = listOf()
) : IEntity<String>

data class Invoice(
    override val id: String = IDUtils.newID(),
    val orderID: String = "",
    val supplier: Supplier? = null,
    val receiver: String = "",
    val dateTime: LocalDateTime? = null,
    val totalPrice: Float = 0f,
    val currency: String = ""
) : IEntity<String> {
    override fun toString(): String {
        return "$supplier $orderID $dateTime"
    }
}

data class EntityCountable<T : IEntity<*>> constructor(
    val entity: T,
    val count: Long
)

data class EntityValuable<T : IEntity<*>> constructor(
    val entity: T,
    val value: String? = null,
    val factor: Int? = null
)


interface IParentableEntity<ID, T : IParentableEntity<ID, T>> : IEntity<ID> {
    val parentEntity: T?
}

fun <ID, T : IParentableEntity<ID, T>> T.iterate(
    ids: List<ID> = listOf(),
    doOnIteration: (T) -> kotlin.Unit
) {
    //1. do on this:
    log("doOnIteration for $this. id: ${this.id} parent: $parentEntity")
    doOnIteration(this)

    //2. do for parent
    parentEntity?.let { pe ->
        if (!ids.contains(pe.id)) {
            pe.iterate(ids.plus(this.id), doOnIteration)
        } else {
            log("Warning: inheritance loop! Check $pe")
        }
    }
}