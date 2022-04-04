package persistence.datasources.exposed

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.BaseDao
import persistence.datasources.GroupedResult
import persistence.dto.exposed.Tables
import persistence.repository.Specification

abstract class BaseDao<
        ENTITY : IEntity<*>,
        EXPOSED_ID : Comparable<EXPOSED_ID>,
        EXPOSED_ENTITY : Entity<EXPOSED_ID>,
        EXPOSED_ENTITY_CLASS : EntityClass<EXPOSED_ID, EXPOSED_ENTITY>
        >(
    protected val table: IdTable<EXPOSED_ID>,
    private val entityClass: EXPOSED_ENTITY_CLASS
) :
    BaseDao<ENTITY> {


    /**
     * Resulting table in GroupingSlice must contain entity's id (table.id)
     */
    protected abstract fun getTableField(fieldID: EntityFieldID): GroupingSlice

    abstract fun mapToEntity(exposedEntity: EXPOSED_ENTITY): ENTITY


//    abstract fun mapToExposed(entity: ENTITY): EXPOSED_ENTITY

    abstract fun Transaction.mapIntoExposed(exposed: EXPOSED_ENTITY, entity: ENTITY)

    abstract fun mapToID(id: Any): EXPOSED_ID

    override suspend fun query(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<GroupedResult<ENTITY>> {
        return newSuspendedTransaction {
            if (groupingSpec == null) {
                getNotGroupedResult(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec)
            } else {
                getGroupedResult(
                    groupingSpec = groupingSpec,
                    filterSpec = filterSpec,
                    sortingSpec = sortingSpec,
                    pagingSpec = pagingSpec
                )
            }
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
            entityClass.new {
                mapIntoExposed(this, entity)
            }
            commit()
        }
    }

    override suspend fun update(entity: ENTITY) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val currentEntity = entity.id?.let { entityClass[mapToID(it)] }
            currentEntity?.let {
                mapIntoExposed(it, entity)
            }
            commit()
        }
    }

    override suspend fun getItemsCount(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Long {
        return newSuspendedTransaction {
            if (groupingSpec == null) {
                getNotGroupedQuery(filterSpec, sortingSpec, pagingSpec).count()
            } else {
                getGroupedResultGroups(
                    groupingSpec = groupingSpec,
                    filterSpec = filterSpec,
                    sortingSpec = sortingSpec,
                    pagingSpec = pagingSpec
                ).size.toLong()
            }
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val currentEntity = entityClass[mapToID(id)]
            currentEntity.delete()
            commit()
        }
    }

    private fun Transaction.getNotGroupedResult(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<GroupedResult<ENTITY>> {
        val query = getNotGroupedQuery(filterSpec = filterSpec, sortingSpec = sortingSpec, pagingSpec = pagingSpec)
        return listOf(GroupedResult("default", entityClass.wrapRows(query).map { mapToEntity(it) }))
    }

    private fun Transaction.getNotGroupedQuery(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Query {
        val query = table.selectAll()
        (filterSpec as? Specification.Filters)?.let {
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

    private fun Transaction.getGroupedResult(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<GroupedResult<ENTITY>> {

        val groupsKeys = getGroupedResultGroups(
            groupingSpec = groupingSpec,
            filterSpec = filterSpec,
            sortingSpec = sortingSpec,
            pagingSpec = pagingSpec
        )

        return listOf()
    }

    /**
     * Returned grouped result in a form of the list of pair:
     * ID of
     */
    private fun Transaction.getGroupedResultGroups(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<Pair<Any, List<EXPOSED_ID>>> {
        val query = table.selectAll()
        //1. filtering if it's given
        (filterSpec as? Specification.Filters)?.let {
            query.addFiltering(it)
        }

        //2. sorting if it's given
        (sortingSpec as? Specification.Sorted)?.let {
            query.addSorting(it)
        }
        //3. grouping if it's given
        return (groupingSpec as? Specification.Grouped)?.let {
            val groupByFieldID = it.spec.fieldID
            // get all values for that fieldID:
            // need a table and field in Exposed for fieldID
            val groupSlice = getTableField(groupByFieldID)

            //using DSL - get all values for given column
            //convert list of pairs <a,b> to list of pair<"key", list<entity id>>
            groupSlice
                .table
                .slice(groupSlice.groupColumn, table.id)
                .selectAll()
                .distinct()
                .mapNotNull { result ->
                    result[groupSlice.groupColumn]?.let { gc -> gc to result[table.id] as EXPOSED_ID }
                } //https://stackoverflow.com/questions/53433108/kotlin-from-a-list-of-maps-to-a-map-grouped-by-key
                .groupBy({ entry -> entry.first }, { entry -> entry.second })
                .toList()
        } ?: throw IllegalArgumentException("groupingSpec must be Specification.Grouped class instance")
    }

    protected inner class GroupingSlice(
        val table: ColumnSet,
        val groupColumn: Column<*>
    )

    private fun Query.addFiltering(filterSpec: Specification.Filters) {

        val filters = filterSpec.filters.mapNotNull { spec ->
            if (spec.entityClass != Container::class) return@mapNotNull null

            when (spec.fieldID.tag) {
                //name
                EntityFieldID.tag_name -> spec.filteredValues.mapNotNull {
                    (it as? EntityField.StringField)?.value?.let { name ->
                        ExposedFilter(Tables.Containers.name, name)
                    }
                }
                //description
                EntityFieldID.tag_description -> spec.filteredValues.mapNotNull {
                    (it as? EntityField.StringField)?.value?.let { description ->
                        ExposedFilter(Tables.Containers.description, description)
                    }
                }
                else -> null
            }
        }.flatten()

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