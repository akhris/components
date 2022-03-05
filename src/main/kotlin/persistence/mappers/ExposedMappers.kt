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
        item = item?.let { EntityCountable(it.toItem(), count ?: 0L) },
        container = container?.toContainer(),
        dateTime = dateTime,
        supplier = supplier?.toSupplier()
    )
}

fun EntityItem.toItem(): Item {
    return Item(
        id = id.value.toString(),
        name = name,
        type = type?.toObjectType(),
        values = values.map { it.toValue() }
    )
}

fun EntityContainer.toContainer(): Container {
    return Container(
        id = id.value.toString(),
        name = name,
        description = description,
        parentContainer = parents.firstOrNull()?.toContainer()
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

fun EntityObjectType.toObjectType(): ObjectType {
    return ObjectType(
        id = id.value.toString(),
        name = name,
        parameters = parameters.map { it.toParameter() }
    )
}

fun EntityValue.toValue(): Value {
    return Value(
        id = id.value.toString(),
        parameter = parameter.toParameter(),
        value = value,
        factor = factor
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