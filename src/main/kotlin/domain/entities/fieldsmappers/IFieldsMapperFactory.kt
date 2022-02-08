package domain.entities.fieldsmappers

import com.akhris.domain.core.entities.IEntity
import domain.entities.*
import domain.entities.Unit
import kotlin.reflect.KClass

interface IFieldsMapperFactory {
    fun <T : IEntity<*>> getFieldsMapper(entityClass: KClass<out T>): IFieldsMapper
}

class FieldsMapperFactory : IFieldsMapperFactory {
    override fun <T : IEntity<*>> getFieldsMapper(entityClass: KClass<out T>): IFieldsMapper {
        return when (entityClass) {
            Unit::class -> UnitFieldsMapper()
            Parameter::class -> ParameterFieldsMapper()
            ObjectType::class -> ObjectTypeFieldsMapper()
            Item::class -> ItemFieldsMapper()
            Value::class -> ValueFieldsMapper()
            Container::class -> ContainerFieldsMapper()
            else -> throw IllegalArgumentException("$this cannot get factory for $entityClass")
        }
    }
}