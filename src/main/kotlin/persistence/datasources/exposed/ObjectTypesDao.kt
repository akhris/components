package persistence.datasources.exposed

import domain.entities.ObjectType
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityObjectType
import persistence.dto.exposed.Tables
import persistence.mappers.toObjectType
import utils.toUUID

class ObjectTypesDao(columnMappersFactory: ColumnMappersFactory) :
    BaseUUIDDao<ObjectType, EntityObjectType, Tables.ObjectTypes>(
        table = Tables.ObjectTypes,
        entityClass = EntityObjectType,
        columnMapper = columnMappersFactory.getColumnMapper(ObjectType::class)
    ) {
    override fun mapToEntity(exposedEntity: EntityObjectType): ObjectType = exposedEntity.toObjectType()

    override fun insertStatement(entity: ObjectType): Tables.ObjectTypes.(InsertStatement<Number>) -> Unit = {
        it[name] = entity.name
        it[parent] = entity.parentEntity?.id?.toUUID()
    }

    override fun updateStatement(entity: ObjectType): Tables.ObjectTypes.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[parent] = entity.parentEntity?.id?.toUUID()
    }

    override fun Transaction.doAfterUpdate(entity: ObjectType) {
        //3. if parameters list changed - update it:

        //remove all old parameters
        Tables
            .ParametersToObjectType
            .deleteWhere { Tables.ParametersToObjectType.objectType eq entity.id.toUUID() }

        //batch insert all new parameters
        Tables.ParametersToObjectType.batchInsert(entity.parameters) { p ->
            this[Tables.ParametersToObjectType.objectType] = entity.id.toUUID()
            this[Tables.ParametersToObjectType.parameter] = p.id.toUUID()
        }

    }

}