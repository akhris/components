package persistence.dto.exposed

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*


class EntityItem(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityItem>(Tables.Items)

    var name by Tables.Items.name
//    var type by Tables.Items.type

    var type by EntityObjectType optionalReferencedOn (Tables.Items.type)
    val values by EntityValue via Tables.ValuesToItem
}

class EntityObjectType(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityObjectType>(Tables.ObjectTypes)

    var name by Tables.ObjectTypes.name
    val parameters by EntityParameter via Tables.ParametersToObjectType
}


class EntityValue(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityValue>(Tables.Values)

//    var parameter by Tables.Values.parameter
    var parameter by EntityParameter optionalReferencedOn Tables.Values.parameter
    var value by Tables.Values.value
    var factor by Tables.Values.factor
}

class EntityParameter(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityParameter>(Tables.Parameters)

    var name by Tables.Parameters.name
    var description by Tables.Parameters.description

    //    val unit by Tables.Parameters.unit
    val unit by EntityUnit optionalReferencedOn (Tables.Parameters.unit)
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
    var parents by EntityContainer.via(Tables.ContainerToContainers.child, Tables.ContainerToContainers.parent)
    var children by EntityContainer.via(Tables.ContainerToContainers.parent, Tables.ContainerToContainers.child)

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

    var item by EntityItem optionalReferencedOn (Tables.ItemIncomes.item)
    var count by Tables.ItemIncomes.count
//    var container by Tables.ItemIncomes.container

    var container by EntityContainer optionalReferencedOn Tables.ItemIncomes.container
    var dateTime by Tables.ItemIncomes.dateTime

    //    var supplier by Tables.ItemIncomes.supplier
    var supplier by EntitySupplier optionalReferencedOn Tables.ItemIncomes.supplier

}

class EntityItemOutcome(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityItemOutcome>(Tables.ItemOutcomes)

//    var item by Tables.ItemOutcomes.item

    var item by EntityItem optionalReferencedOn (Tables.ItemOutcomes.item)
    var count by Tables.ItemOutcomes.count
//    var container by Tables.ItemOutcomes.container

    var container by EntityContainer optionalReferencedOn Tables.ItemOutcomes.container
    var dateTime by Tables.ItemOutcomes.dateTime

}

class EntityProject(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityProject>(Tables.Projects)

    var name by Tables.Projects.name
    var description by Tables.Projects.description
    val items by EntityProjectItem referrersOn Tables.ProjectItems.project
}

class EntityProjectItem(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityProjectItem>(Tables.ProjectItems)

    var project by EntityProject referencedOn Tables.ProjectItems.project
    var item by EntityItem referencedOn Tables.ProjectItems.item
    var count by Tables.ProjectItems.count
}