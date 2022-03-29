package persistence.datasources.exposed

import com.akhris.domain.core.repository.ISpecification
import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.datasources.IBaseGSFPDao
import persistence.dto.exposed.EntityContainer
import persistence.dto.exposed.Tables
import persistence.mappers.toContainer
import persistence.repository.IGSFPRepository
import persistence.repository.Specification

class ContainersGSFPDao : IBaseGSFPDao<Container> {

    override suspend fun query(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<IGSFPRepository.Result<Container>> {
        val a = newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. initial query - select all items
            val query = Tables.Containers.selectAll()

            //2. if filterSpec is given - add filtering to query
            query.addFiltering(filterSpec)

            //3. if sortingSpec is given - add sorting to query
            query.addSorting(sortingSpec)

            //4. if groupingSpec is given - add grouping to query
            if (groupingSpec == null) {
                //if there is no grouping - add paging to query (if pagingSpec is given)
                query.addPaging(pagingSpec)
            }

            //5. query results:
            val containers = EntityContainer.wrapRows(query).map { it.toContainer() }

            //6. group and paginate results.
            // if groupingSpec is given - group using it. If not - whole list is moved to "default" group
            val grouped = containers
                .groupBy { c ->
                    when ((groupingSpec as? Specification.Grouped)?.spec?.fieldID?.tag) {
                        //name
                        EntityFieldID.tag_name -> c.name
                        //description
                        EntityFieldID.tag_description -> c.description
                        else -> "default"
                    }
                }
                .map { (k, v) ->
                    IGSFPRepository.Result(k, v)
                }

            //7. if there is more than one group and pagingSpec is given - return number of groups according to pagingSpec
            if (containers.size > 1 && pagingSpec != null && pagingSpec is Specification.Paginated) {

                val fromIndex = (pagingSpec.itemsPerPage * (pagingSpec.pageNumber - 1)).toInt()
                val toIndex = fromIndex + pagingSpec.itemsPerPage.toInt()

                grouped
                    .subList(fromIndex = fromIndex, toIndex = toIndex)
            } else {
                //if there is one group or pagingSpec is not given - return grouped items
                grouped
            }

        }
        return a
    }

    private fun Query.addFiltering(filterSpec: ISpecification?) {
        if (filterSpec !is Specification.Filters) {
            return
        }

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


    private fun Query.addSorting(sortingSpec: ISpecification?) {
        if (sortingSpec !is Specification.Sorted) {
            return
        }

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


    private fun Query.addPaging(pagingSpec: ISpecification?) {
        if (pagingSpec !is Specification.Paginated) {
            return
        }
        limit(n = pagingSpec.itemsPerPage.toInt(), offset = pagingSpec.itemsPerPage * (pagingSpec.pageNumber - 1))
    }

    override suspend fun getItemsCount(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): Long {
        TODO("Not yet implemented")
    }

}