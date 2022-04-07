package persistence.datasources.exposed

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IBaseDao
import persistence.dto.exposed.Tables
import persistence.repository.Specification
import java.util.*

abstract class BaseDao<
        ENTITY : IEntity<*>,
        EXPOSED_ID : Comparable<EXPOSED_ID>,
        EXPOSED_ENTITY : Entity<EXPOSED_ID>,
        EXPOSED_TABLE : IdTable<EXPOSED_ID>
        >(
    protected val table: EXPOSED_TABLE,
    private val entityClass: EntityClass<EXPOSED_ID, EXPOSED_ENTITY>
) :
    IBaseDao<ENTITY> {


    abstract fun mapToEntity(exposedEntity: EXPOSED_ENTITY): ENTITY

//    abstract fun Transaction.mapIntoExposed(exposed: EXPOSED_ENTITY, entity: ENTITY)

    protected abstract fun mapToID(id: Any): EXPOSED_ID

    protected abstract fun insertStatement(entity: ENTITY): EXPOSED_TABLE.(InsertStatement<Number>) -> Unit
    protected open fun Transaction.doAfterInsert(entity: ENTITY) {}

    protected abstract fun updateStatement(entity: ENTITY): EXPOSED_TABLE.(UpdateStatement) -> Unit
    protected open fun Transaction.doAfterUpdate(entity: ENTITY) {}


    override suspend fun getItemsCount(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Long {
        return newSuspendedTransaction {
            getNotGroupedQuery(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec).count()
        }
    }

    override suspend fun query(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<ENTITY> {
        return newSuspendedTransaction {
            val query = getNotGroupedQuery(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec)
            entityClass.wrapRows(query).map { mapToEntity(it) }
        }
    }

    override suspend fun getByID(id: String): ENTITY? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                mapToEntity(entityClass.get(id = mapToID(id)))
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun insert(entity: ENTITY) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            table.insert { statement ->
                entity.id?.let { statement[id] = mapToID(it) }
                insertStatement(entity).invoke(table, statement)
            }
            doAfterInsert(entity)
            commit()
        }
    }


    override suspend fun update(entity: ENTITY) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            table.update(where = entity.id?.let { { table.id eq mapToID(it) } }, body = updateStatement(entity))
            doAfterUpdate(entity)
//            val currentEntity = entity.id?.let { entityClass[mapToID(it)] }
//            currentEntity?.let {
//                mapIntoExposed(it, entity)
//            }
            commit()
        }
    }


    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            table.deleteWhere { table.id eq mapToID(id) }
//            entityClass[mapToID(id)].delete()
            commit()
        }
    }

    private fun Transaction.getNotGroupedQuery(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Query {
        val query = table.selectAll()
        (filterSpec as? Specification.Filtered)?.let {
            query.addFiltering(it)
        }
        (sortingSpec as? Specification.Sorted)?.let {
            query.addSorting(it)
        }
        //if there is no grouping - add paging to query (if pagingSpec is given)
        (pagingSpec as? Specification.Paginated)?.let {
            query.addPaging(pagingSpec)
        }
        return query
    }

    protected open val filter: ((EntityField) -> ExposedFilter<Any>?)? = null

    private fun Query.addFiltering(filterSpec: Specification.Filtered) {

        val filters =
            filterSpec
                .filters
                .flatMap { spec ->
                    spec.filteredValues.mapNotNull { filteredField ->
                        filter?.invoke(filteredField)
                    }
                }

        filters.forEach {
            orWhere { it.first eq it.second }
        }
    }


    private fun Query.addSorting(sortingSpec: Specification.Sorted) {

        when (sortingSpec.spec.fieldID.tag) {
            //name
            EntityFieldID.tag_name -> {
                orderBy(
                    Tables.Containers.name,
                    order = if (sortingSpec.spec.isAscending) SortOrder.ASC else SortOrder.DESC
                )
            }
            //description
            EntityFieldID.tag_description -> {
                orderBy(
                    Tables.Containers.description,
                    order = if (sortingSpec.spec.isAscending) SortOrder.ASC else SortOrder.DESC
                )
            }
        }
    }


    private fun Query.addPaging(pagingSpec: Specification.Paginated) {
        limit(n = pagingSpec.itemsPerPage.toInt(), offset = pagingSpec.itemsPerPage * (pagingSpec.pageNumber - 1))
    }

}

abstract class BaseUUIDDao<ENTITY : IEntity<*>,
        EXPOSED_ENTITY : Entity<UUID>,
        EXPOSED_TABLE : IdTable<UUID>>(table: EXPOSED_TABLE, entityClass: EntityClass<UUID, EXPOSED_ENTITY>) :
    BaseDao<ENTITY, UUID, EXPOSED_ENTITY, EXPOSED_TABLE>(table, entityClass) {
    override fun mapToID(id: Any): UUID {
        return if (id is String) {
            UUID.fromString(id)
        } else {
            throw IllegalArgumentException("ID must be String")
        }
    }
}