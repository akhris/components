package persistence.repository

import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.ItemIncome
import domain.entities.ItemOutcome
import domain.entities.WarehouseItem
import persistence.datasources.ListItem
import persistence.datasources.getGroupedMap

/**
 * repository that handles items in warehouse.
 * This repo is dependent on [IItemIncomeRepository] and [IItemOutcomeRepository], it calculates overall items count as
 * incomes minus outcomes.
 * This repo is read-only, so only query function is overriden.
 */
class WarehouseItemRepository(
    private val incomeRepository: BaseRepository<ItemIncome>,
    private val outcomeRepository: BaseRepository<ItemOutcome>
) : IWarehouseItemRepository {

    override suspend fun getByID(id: String): WarehouseItem {
        throw UnsupportedOperationException(errorText)
    }

    override suspend fun insert(t: WarehouseItem) {
        throw UnsupportedOperationException(errorText)
    }


    override suspend fun query(specification: ISpecification): List<WarehouseItem> {
        return emptyList()
    }

    override suspend fun remove(t: WarehouseItem) {
        throw UnsupportedOperationException(errorText)
    }

    override suspend fun remove(specification: ISpecification) {
        throw UnsupportedOperationException(errorText)
    }

    override suspend fun update(t: WarehouseItem) {
        throw UnsupportedOperationException(errorText)
    }

    override suspend fun gQuery(specification: ISpecification): List<ListItem<WarehouseItem>> {


        return getMergedLists(specification)

        //get all incomes by specification
        val incomes =
            kotlin
                .runCatching { incomeRepository.gQuery(specification) }
                .getOrElse { listOf() }
                .getGroupedMap()

        //get all outcomes by specification
        val outcomes =
            kotlin.runCatching { outcomeRepository.gQuery(specification) }
                .getOrElse { listOf() }
                .getGroupedMap()



        log("got incomes size: ${incomes.size}")
        log("got outcomes size: ${outcomes.size}")

        //merge maps like here: https://stackoverflow.com/questions/54232530/merge-values-in-map-kotlin
        //also count overall presence of item using fold
        val overall =
            (incomes.asSequence() + outcomes.asSequence()).groupBy({ it.key }, { entry ->
                entry.value.mapNotNull {
                    when (it) {
                        is ItemOutcome -> WarehouseItem(
                            item = it.item?.copy(count = -it.item.count),
                            container = it.container
                        )
                        is ItemIncome -> WarehouseItem(item = it.item, container = it.container)
                        else -> null
                    }
                }
            }).mapValues { it.value.flatten() }
                .map { entry ->
                    ListItem.GroupedItem(
                        key = entry.key,
                        items = entry.value,
                        categoryName = entry.key?.toString() ?: ""
                    )
                }


//                .mapValues { (_, values) ->
//                    values.flatten().groupBy(keySelector = { p -> p.first }, valueTransform = { p -> p.second })
//                        .mapValues { (_, values) -> values.fold(0L) { a, c -> a + c } }
//                }

        //return overall map that is flatten by creating WarehouseItem for each container
        return overall


    }

    private suspend fun getMergedLists(specification: ISpecification): List<ListItem<WarehouseItem>> {
        //get all incomes by specification
        val incomes =
            kotlin
                .runCatching { incomeRepository.gQuery(specification) }
                .getOrElse { listOf() }
//                .map { listItem ->
//                    when (listItem) {
//                        is ListItem.GroupedItem -> ListItem.GroupedItem(
//                            key = listItem.key,
//                            keyName = listItem.keyName,
//                            categoryName = listItem.categoryName,
//                            items = listItem.items.map { it.toWarehouseItem() })
//                        is ListItem.NotGroupedItem -> ListItem.NotGroupedItem(listItem.item.toWarehouseItem())
//                    }
//                }


        //get all outcomes by specification
        val outcomes =
            kotlin.runCatching { outcomeRepository.gQuery(specification) }
                .getOrElse { listOf() }
//                .map { listItem ->
//                    when (listItem) {
//                        is ListItem.GroupedItem -> ListItem.GroupedItem(
//                            key = listItem.key,
//                            keyName = listItem.keyName,
//                            categoryName = listItem.categoryName,
//                            items = listItem.items.map { it.toWarehouseItem() })
//                        is ListItem.NotGroupedItem -> ListItem.NotGroupedItem(listItem.item.toWarehouseItem())
//                    }
//                }


       


    }


    companion object {
        private const val errorText =
            """IWarehouseItemRepository is read-only repository, only query function must be used.
                |To insert/remove/update items use IItemIncomeRepository or IItemOutcomeRepository"""
    }

}

private fun ItemIncome.toWarehouseItem(): WarehouseItem = WarehouseItem(item = this.item, container = this.container)
private fun ItemOutcome.toWarehouseItem(): WarehouseItem =
    WarehouseItem(item = this.item?.copy(count = -this.item.count), container = this.container)