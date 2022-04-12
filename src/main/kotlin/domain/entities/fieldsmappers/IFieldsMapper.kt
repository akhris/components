package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity

interface IFieldsMapper<T : IEntity<*>> {
    //    fun mapFields(entity: Any): List<EntityField>
    fun getEntityIDs(): List<EntityFieldID>
    fun getFieldByID(entity: T, fieldID: EntityFieldID): EntityField?
    fun mapIntoEntity(entity: T, field: EntityField): T
}


//fun List<EntityFieldID>.flatten(): List<EntityFieldID> {
//    return flatMap { fid ->
//        when (fid) {
//            is EntityFieldID.EntitiesListID -> fid.entitiesIDs
//            else -> listOf(fid)
//        }
//    }
//}

