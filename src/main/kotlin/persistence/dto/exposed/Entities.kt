package persistence.dto.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.*


class EntityItem(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityItem>(Tables.Items)

    var name by Tables.Items.name
//    var type by Tables.Items.type

    var type by EntityObjectType optionalReferencedOn (Tables.Items.type)
    val values by EntityItemValue referrersOn Tables.ItemValues.item
}

class EntityItemValue(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityItemValue>(Tables.ItemValues)

    var item by EntityItem referencedOn Tables.ItemValues.item
    var parameter by EntityParameter referencedOn Tables.ItemValues.parameter
    var value by Tables.ItemValues.value
    var factor by Tables.ItemValues.factor
    var position by Tables.ItemValues.position
}

class EntityObjectType(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityObjectType>(Tables.ObjectTypes)

    var name by Tables.ObjectTypes.name
    val parameters by EntityParameter via Tables.ParametersToObjectType

    val parent by EntityObjectType optionalReferencedOn (Tables.ObjectTypes.parent)

//    override var parents by EntityObjectType.via(Tables.ObjectTypeToObjectTypes.child, Tables.ObjectTypeToObjectTypes.parent)
//    override var children by EntityObjectType.via(Tables.ObjectTypeToObjectTypes.parent, Tables.ObjectTypeToObjectTypes.child)
}


//class EntityValue(id: EntityID<UUID>) : UUIDEntity(id) {
//    companion object : UUIDEntityClass<EntityValue>(Tables.Values)
//
//    //    var parameter by Tables.Values.parameter
//    var parameter by EntityParameter optionalReferencedOn Tables.Values.parameter
//    var value by Tables.Values.value
//    var factor by Tables.Values.factor
//}

class EntityParameter(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityParameter>(Tables.Parameters)

    var name by Tables.Parameters.name
    var description by Tables.Parameters.description

    //    val unit by Tables.Parameters.unit
    var unit by EntityUnit optionalReferencedOn (Tables.Parameters.unit)
}


class EntityUnit(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityUnit>(Tables.Units)

    var unit by Tables.Units.unit
    var isMultipliable by Tables.Units.isMultipliable
}

/**
 * https://github.com/JetBrains/Exposed/wiki/DAO#parent-child-reference
 */
class EntityContainer(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityContainer>(Tables.Containers)

    var name by Tables.Containers.name
    var description by Tables.Containers.description
    val parent by EntityContainer optionalReferencedOn (Tables.Containers.parent)
}

class EntitySupplier(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntitySupplier>(Tables.Suppliers)

    var name by Tables.Suppliers.name
    var description by Tables.Suppliers.description
    var url by Tables.Suppliers.url
    var isFavorite by Tables.Suppliers.isFavorite

}

class EntityItemIncome(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityItemIncome>(Tables.ItemIncomes)

//    var item by Tables.ItemIncomes.item

    var item by EntityItem referencedOn Tables.ItemIncomes.item
    var count by Tables.ItemIncomes.count
//    var container by Tables.ItemIncomes.container

    var container by EntityContainer optionalReferencedOn Tables.ItemIncomes.container
    var dateTime by Tables.ItemIncomes.dateTime

    //    var supplier by Tables.ItemIncomes.supplier
    var supplier by EntitySupplier optionalReferencedOn Tables.ItemIncomes.supplier

    var invoice by EntityInvoice optionalReferencedOn Tables.ItemIncomes.invoice
}

class EntityItemOutcome(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityItemOutcome>(Tables.ItemOutcomes)

//    var item by Tables.ItemOutcomes.item

    var item by EntityItem referencedOn (Tables.ItemOutcomes.item)
    var count by Tables.ItemOutcomes.count
//    var container by Tables.ItemOutcomes.container

    var container by EntityContainer optionalReferencedOn Tables.ItemOutcomes.container
    var dateTime by Tables.ItemOutcomes.dateTime

}

class EntityProject(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityProject>(Tables.Projects)

    var name by Tables.Projects.name
    var description by Tables.Projects.description
    var dateTime by Tables.Projects.dateTime
    val extFile by Tables.Projects.extFile
    val items by EntityProjectItem referrersOn Tables.ProjectItems.project
}

class EntityProjectItem(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityProjectItem>(Tables.ProjectItems)

    var project by EntityProject referencedOn Tables.ProjectItems.project
    var item by EntityItem referencedOn Tables.ProjectItems.item
    var count by Tables.ProjectItems.count
}

class EntityInvoice(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityInvoice>(Tables.Invoices)

    val orderID by Tables.Invoices.orderID
    val supplier by EntitySupplier optionalReferencedOn Tables.Invoices.supplier
    val receiver by Tables.Invoices.receiver
    val dateTime by Tables.Invoices.dateTime
    val totalPrice by Tables.Invoices.totalPrice
    val currency by Tables.Invoices.currency

}

interface IParentableEntity<T> {
    val parents: SizedIterable<T>
    val children: SizedIterable<T>
}
