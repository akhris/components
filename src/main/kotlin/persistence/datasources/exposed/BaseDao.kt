package persistence.datasources.exposed

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.*
import persistence.datasources.BaseDao
import persistence.dto.exposed.Tables
import persistence.repository.Specification

abstract class BaseDao<ENTITY : IEntity<*>>(private val table: Table) : BaseDao<ENTITY> {


    abstract fun <T> getTableField(fieldID: EntityFieldID): Pair<Table, Column<T>>

    abstract fun getExposedEntityField(fieldID: EntityFieldID): EntityClass<*, *>

    private fun Query.specifyNotGrouping(
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ) {
        (filterSpec as? Specification.Filters)?.let {
            addFiltering(it)
        }
        (sortingSpec as? Specification.Sorted)?.let {
            addSorting(it)
        }
        //if there is no grouping - add paging to query (if pagingSpec is given)
        (pagingSpec as? Specification.Paginated)?.let {
            addPaging(pagingSpec)
        }
    }

    private fun specifyGrouping(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ) {

        val query = table.selectAll()
        //1. filtering if it's given
        (filterSpec as? Specification.Filters)?.let {
            query.addFiltering(it)
        }
        //2. grouping if it's given
        (groupingSpec as? Specification.Grouped)?.let {
            val groupByFieldID = it.spec.fieldID
            // get all values for that fieldID:
            // need a table and field in Exposed for fieldID
            val (tempTable, column) = getTableField<Any>(groupByFieldID)
            //using DAO?
            val exposedEntity = getExposedEntityField(groupByFieldID)
            exposedEntity.all()

            //using DSL?
            val valuesForFieldID = tempTable.slice(column).selectAll().mapNotNull { result -> result[column] }

            //for each value for given field ID need to find items
            val a = valuesForFieldID.map {
                query.orWhere { column eq it }
            }

        }
    }

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