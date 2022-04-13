package persistence.datasources.exposed

import domain.entities.Supplier
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntitySupplier
import persistence.dto.exposed.Tables
import persistence.mappers.toSupplier

class SuppliersDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Supplier, EntitySupplier, Tables.Suppliers>(
    table = Tables.Suppliers,
    entityClass = EntitySupplier,
    columnMapper = columnMappersFactory.getColumnMapper(Supplier::class)
){
    override fun mapToEntity(exposedEntity: EntitySupplier): Supplier = exposedEntity.toSupplier()

    override fun insertStatement(entity: Supplier): Tables.Suppliers.(InsertStatement<Number>) -> Unit = {
        it[name] = entity.name
        it[url] = entity.url
        it[description] = entity.description
        it[isFavorite] = entity.isFavorite
    }

    override fun updateStatement(entity: Supplier): Tables.Suppliers.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[url] = entity.url
        it[description] = entity.description
        it[isFavorite] = entity.isFavorite
    }

}






/*
    override suspend fun getByID(id: String): Supplier? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntitySupplier.get(id = UUID.fromString(id)).toSupplier()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(filters: List<FilterSpec>): List<Supplier> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            EntitySupplier.all().map { it.toSupplier() }
        }
    }

    override suspend fun insert(entity: Supplier) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Suppliers.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[url] = entity.url
                statement[description] = entity.description
                statement[isFavorite] = entity.isFavorite
            }
            commit()
        }
    }

    override suspend fun update(entity: Supplier) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val foundEntity = EntitySupplier[UUID.fromString(entity.id)]
            //2. update it:
            foundEntity.name = foundEntity.name
            foundEntity.url = foundEntity.url
            foundEntity.description = foundEntity.description
            foundEntity.isFavorite = foundEntity.isFavorite
            commit()
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val entity = EntitySupplier[UUID.fromString(id)]
            //2. delete it
            entity.delete()
            commit()
        }
    }

     */