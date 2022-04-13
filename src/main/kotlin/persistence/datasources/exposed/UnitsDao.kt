package persistence.datasources.exposed

import domain.entities.Unit
import domain.entities.fieldsmappers.EntityField
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityUnit
import persistence.dto.exposed.Tables
import persistence.mappers.toUnit

class UnitsDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Unit, EntityUnit, Tables.Units>(
    table = Tables.Units,
    entityClass = EntityUnit,
    columnMapper = columnMappersFactory.getColumnMapper(domain.entities.Unit::class)
) {


    override fun mapToEntity(exposedEntity: EntityUnit): Unit {
        return exposedEntity.toUnit()
    }

    override fun insertStatement(entity: Unit): Tables.Units.(InsertStatement<Number>) -> kotlin.Unit = {
        it[unit] = entity.unit
        it[isMultipliable] = entity.isMultipliable
    }

    override fun updateStatement(entity: Unit): Tables.Units.(UpdateStatement) -> kotlin.Unit = {
        it[unit] = entity.unit
        it[isMultipliable] = entity.isMultipliable
    }

    override val filter: ((EntityField) -> ExposedFilter<Any>?)
        get() = { entityField ->
            when (entityField) {
                is EntityField.StringField -> ExposedFilter(Tables.Units.unit, entityField.value)
                is EntityField.BooleanField -> ExposedFilter(Tables.Units.isMultipliable, entityField.value)
                else -> null
            } as? ExposedFilter<Any>?
        }


}


/*
    override suspend fun getByID(id: String): Unit? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityUnit.get(id = UUID.fromString(id)).toUnit()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(filters: List<FilterSpec>): List<Unit> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntityUnit.all().map { it.toUnit() }
        }
    }

    override suspend fun insert(entity: Unit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Units.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[unit] = entity.unit
                statement[isMultipliable] = entity.isMultipliable
            }
            commit()
        }
    }

    override suspend fun update(entity: Unit) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val foundEntity = EntityUnit[UUID.fromString(entity.id)]
            //2. update it:
            foundEntity.unit = entity.unit
            foundEntity.isMultipliable = entity.isMultipliable
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val entity = EntityUnit[UUID.fromString(id)]
            //2. delete it
            entity.delete()
            commit()
        }
    }

     */