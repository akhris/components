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
//        parentChildTable = Tables.ObjectTypeToObjectTypes
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


/*
    override suspend fun getByID(id: String): ObjectType? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityObjectType.get(id = UUID.fromString(id)).toObjectType()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(filters: List<FilterSpec>): List<ObjectType> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityObjectType.all().map { it.toObjectType() }
        }
    }

    override suspend fun insert(entity: ObjectType) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.ObjectTypes.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
            }
            Tables.ParametersToObjectType.batchInsert(entity.parameters) { p ->
                this[Tables.ParametersToObjectType.objectType] = entity.id.toUUID()
                this[Tables.ParametersToObjectType.parameter] = p.id.toUUID()
            }
            entity.parentObjectType?.let { pot ->
                Tables.ObjectTypeToObjectTypes.insert { statement ->
                    statement[parent] = pot.id.toUUID()
                    statement[child] = entity.id.toUUID()
                }
            }
            commit()
        }
    }

    override suspend fun update(entity: ObjectType) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)

            //1. get entity by id:
            val objectType = EntityObjectType[UUID.fromString(entity.id)]
            //2. update it:
            objectType.name = entity.name

            //3. if parameters list changed - update it:
            if (entity.parameters.map { it.id } != objectType.parameters.map { it.id.value.toString() }) {
                //remove all old parameters
                Tables
                    .ParametersToObjectType
                    .deleteWhere { Tables.ParametersToObjectType.objectType eq objectType.id }

                //batch insert all new parameters
                Tables.ParametersToObjectType.batchInsert(entity.parameters) { p ->
                    this[Tables.ParametersToObjectType.objectType] = entity.id.toUUID()
                    this[Tables.ParametersToObjectType.parameter] = p.id.toUUID()
                }
            }

            //3. update children-parent reference:
            if (entity.parentObjectType == null) {
                //delete reference:
                Tables
                    .ObjectTypeToObjectTypes
                    .deleteWhere { Tables.ObjectTypeToObjectTypes.child eq entity.id.toUUID() }
            } else {
                //update reference
                val rowsUpdated = Tables
                    .ObjectTypeToObjectTypes
                    .update({ Tables.ObjectTypeToObjectTypes.child eq entity.id.toUUID() }) {
                        it[parent] = entity.parentObjectType.id.toUUID()
                    }
                //if nothing was updated - insert new row
                if (rowsUpdated == 0) {
                    Tables
                        .ObjectTypeToObjectTypes
                        .insert { statement ->
                            statement[child] = entity.id.toUUID()
                            statement[parent] = entity.parentObjectType.id.toUUID()
                        }
                } else {
                    //do nothing
                }
            }

            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val objectType = EntityObjectType[UUID.fromString(id)]
            objectType.delete()
            commit()
        }
    }

     */