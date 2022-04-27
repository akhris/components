package persistence.mappers

import domain.entities.*
import domain.entities.Unit
import persistence.dto.exposed.*

fun EntityUnit.toUnit(): Unit {
    return Unit(id = id.value.toString(), unit = unit, isMultipliable = isMultipliable)
}

fun EntityItemIncome.toItemIncome(): ItemIncome {
    return ItemIncome(
        id = id.value.toString(),
        item = EntityCountable(item.toItem(), count ?: 0L),
        container = container?.toContainer(),
        dateTime = dateTime,
        supplier = supplier?.toSupplier(),
        invoice = invoice?.toInvoice()
    )
}

fun EntityItemOutcome.toItemOutcome(): ItemOutcome {
    return ItemOutcome(
        id = id.value.toString(),
        item = EntityCountable(item.toItem(), count ?: 0L),
        container = container?.toContainer(),
        dateTime = dateTime
    )
}

fun EntityItem.toItem(): Item {
    return Item(
        id = id.value.toString(),
        name = name,
        type = type?.toObjectType(),
        values = values.sortedBy { it.position }.map { it.toValue() }
    )
}

fun EntityContainer.toContainer(level: Int = 1): Container {
    return Container(
        id = id.value.toString(),
        name = name,
        description = description,
        parentContainer = if (level <= 0) null else parents.firstOrNull()?.toContainer(level - 1)
    )
}

fun EntitySupplier.toSupplier(): Supplier {
    return Supplier(
        id = id.value.toString(),
        name = name,
        description = description,
        url = url,
        isFavorite = isFavorite
    )
}

fun EntityObjectType.toObjectType(level: Int = 1): ObjectType {
    return ObjectType(
        id = id.value.toString(),
        name = name,
        parameters = parameters.map { it.toParameter() },
        parentEntity = if (level <= 0) null else parent?.toObjectType(level - 1)
    )
}

fun EntityParameter.toParameter(): Parameter {
    return Parameter(
        id = id.value.toString(),
        name = name,
        description = description,
        unit = unit?.toUnit()
    )
}

fun EntityProject.toProject(): Project {
    return Project(
        id = id.value.toString(),
        name = name,
        description = description,
        dateTime = dateTime,
        extFile = extFile,
        items = items.map {
            EntityCountable(entity = it.item.toItem(), count = it.count)
        }
    )
}

fun EntityItemValue.toValue(): EntityValuable<Parameter> {
    return EntityValuable(
        entity = this.parameter.toParameter(),
        factor = this.factor,
        value = this.value
    )
}

fun EntityInvoice.toInvoice(): Invoice {
    return Invoice(
        id = id.value.toString(),
        orderID = orderID,
        supplier = supplier?.toSupplier(),
        receiver = receiver,
        dateTime = dateTime,
        totalPrice = totalPrice,
        currency = currency
    )
}