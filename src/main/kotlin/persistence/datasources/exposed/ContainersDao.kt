package persistence.datasources.exposed

import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityContainer
import persistence.dto.exposed.Tables
import persistence.mappers.toContainer
import utils.toUUID


class ContainersDao(columnMappersFactory: ColumnMappersFactory) :
    BaseUUIDDao<Container, EntityContainer, Tables.Containers>(
        table = Tables.Containers,
        entityClass = EntityContainer,
        columnMapper = columnMappersFactory.getColumnMapper(Container::class)
    ) {

    override fun mapToEntity(exposedEntity: EntityContainer): Container = exposedEntity.toContainer()

    override fun insertStatement(entity: Container): Tables.Containers.(InsertStatement<Number>) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[parent] = entity.parentContainer?.id?.toUUID()
    }


    override fun updateStatement(entity: Container): Tables.Containers.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[parent] = entity.parentContainer?.id?.toUUID()
    }


    override val filter: ((EntityField) -> ExposedFilter<Any>?)
        get() = { entityField ->
            when (entityField) {
                //todo need a join somewhere in the top of the querying chain
//                is EntityField.EntityLink -> {
//                    entityField.entity?.id?.let {
//                        ExposedFilter(
//                            Tables.ContainerToContainers.parent,
//                            EntityID(id = mapToID(it), table = table)
//                        )
//                    }
//                }
                is EntityField.StringField -> {
                    when (entityField.fieldID.tag) {
                        EntityFieldID.tag_name -> ExposedFilter(Tables.Containers.name, entityField.value)
                        EntityFieldID.tag_description -> ExposedFilter(Tables.Containers.description, entityField.value)
                        else -> null
                    }
                }
                else -> null
            } as? ExposedFilter<Any>?

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