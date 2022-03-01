package persistence.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*


class EntityUnit(id: EntityID<UUID>):UUIDEntity(id){
    companion object : UUIDEntityClass<EntityUnit>(Tables.Units)
    var unit by Tables.Units.unit
    var isMultipliable by Tables.Units.isMultipliable
}