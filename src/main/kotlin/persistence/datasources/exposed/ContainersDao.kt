package persistence.datasources.exposed

import domain.entities.Container
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import persistence.dto.exposed.EntityContainer
import persistence.dto.exposed.Tables
import persistence.mappers.toContainer
import utils.toUUID
import java.util.*


class ContainersDao : BaseDao<Container, UUID, EntityContainer, EntityContainer.Companion>(
    table = Tables.Containers,
    entityClass = EntityContainer
) {
    override fun getTableField(fieldID: EntityFieldID): GroupingSlice {
        return when (fieldID) {
            is EntityFieldID.EntityID -> GroupingSlice(
                table = table.leftJoin(Tables.ContainerToContainers),
                groupColumn = Tables.ContainerToContainers.parent
            )
            is EntityFieldID.StringID -> when (fieldID.tag) {
                EntityFieldID.tag_name -> GroupingSlice(
                    table = table,
                    groupColumn = Tables.Containers.name
                )
                EntityFieldID.tag_description -> GroupingSlice(
                    table = table,
                    groupColumn = Tables.Containers.description
                )
                else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in Container Entity")
            }
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in Container Entity")
        }
    }

    override fun mapToEntity(exposedEntity: EntityContainer): Container = exposedEntity.toContainer()

    override fun Transaction.mapIntoExposed(exposed: EntityContainer, entity: Container) {
        exposed.name = entity.name
        exposed.description = entity.description

        if (entity.parentContainer == null) {
            Tables.ContainerToContainers.deleteWhere { Tables.ContainerToContainers.child eq entity.id.toUUID() }
        } else {
            Tables.ContainerToContainers.insert { statement ->
                statement[parent] = entity.parentContainer.id.toUUID()
                statement[child] = entity.id.toUUID()
            }
        }
    }

    override fun mapToID(id: Any): UUID {
        return if (id is String) {
            UUID.fromString(id)
        } else {
            throw IllegalArgumentException("ID must be String")
        }
    }

}

/*
class ContainersDao : BaseDao<Container> {
    override suspend fun getByID(id: String): Container? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityContainer.get(id = UUID.fromString(id)).toContainer()
            } catch (e: Exception) {
                log(e)
                null
            }
        }
    }

    override suspend fun getAll(filters: List<FilterSpec>): List<Container> {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            val query = Tables.Containers.selectAll()
            filters.flatMap { it.toExposedFilter() }.forEach {
                query.orWhere { it.first eq it.second }
            }
            EntityContainer.wrapRows(query).map { it.toContainer() }
        }
    }

    override suspend fun insert(entity: Container) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Containers.insert { statement ->
                statement[id] = entity.id.toUUID()
                statement[name] = entity.name
                statement[description] = entity.description
            }
            entity.parentContainer?.let { pC ->
                Tables.ContainerToContainers.insert { statement ->
                    statement[parent] = pC.id.toUUID()
                    statement[child] = entity.id.toUUID()
                }
            }
            commit()
        }
    }

    override suspend fun update(entity: Container) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            //1. get entity by id:
            val container = EntityContainer[entity.id.toUUID()]
            //2. update it:
            container.name = entity.name
            container.description = entity.description
            //3. update children-parent reference:
            if (entity.parentContainer == null) {
                //delete reference:
                Tables
                    .ContainerToContainers
                    .deleteWhere { Tables.ContainerToContainers.child eq entity.id.toUUID() }
            } else {
                //update reference
                val rowsUpdated = Tables
                    .ContainerToContainers
                    .update({ Tables.ContainerToContainers.child eq entity.id.toUUID() }) {
                        it[parent] = entity.parentContainer.id.toUUID()
                    }
                //if nothing was updated - insert new row
                if (rowsUpdated == 0) {
                    Tables
                        .ContainerToContainers
                        .insert { statement ->
                            statement[child] = entity.id.toUUID()
                            statement[parent] = entity.parentContainer.id.toUUID()
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
            val container = EntityContainer[id.toUUID()]
            container.delete()
        }
    }



    override suspend fun query(
        groupingSpec: ISpecification?,
        filterSpec: ISpecification?,
        sortingSpec: ISpecification?,
        pagingSpec: ISpecification?
    ): List<GroupedResult<Container>> {
        return newSuspendedTransaction {


            //1. if groupingSpec!=null


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
                    GroupedResult(k, v)
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

 */