package persistence.repository

import com.akhris.domain.core.repository.ISpecification
import domain.entities.*
import persistence.datasources.EntitiesList
import persistence.datasources.GroupedItem

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

    override suspend fun gQuery(specification: ISpecification): EntitiesList<WarehouseItem> {
        return getMergedLists(specification)
    }


    private suspend fun getMergedLists(specification: ISpecification): EntitiesList<WarehouseItem> {
        //get all incomes by specification

        val queriedIncomes =
            kotlin
                .runCatching { incomeRepository.gQuery(specification) }
                .getOrElse { EntitiesList.empty() }

        val queriedOutcomes =
            kotlin
                .runCatching { outcomeRepository.gQuery(specification) }
                .getOrElse { EntitiesList.empty() }

        //combine incomes and outcomes:
        return if (queriedIncomes is EntitiesList.Grouped && queriedOutcomes is EntitiesList.Grouped) {
            //both are grouped:
            val groups = (queriedIncomes.items.asSequence() + queriedOutcomes.items.asSequence())
                .groupBy(keySelector = { it.groupID }, valueTransform = {
                    it.items
                })
                .mapValues { it.value.flatten() }
                .mapValues { entry ->
                    val incomes = entry.value.filterIsInstance(ItemIncome::class.java)
                    val outcomes = entry.value.filterIsInstance(ItemOutcome::class.java)
                    makeWarehouseList(incomes, outcomes)
                }
                .map { entry ->
                    GroupedItem(entry.key, entry.value)
                }
            EntitiesList.Grouped(groups)
        } else if (queriedIncomes is EntitiesList.NotGrouped && queriedOutcomes is EntitiesList.NotGrouped) {
            //both are not grouped:
            val items = makeWarehouseList(queriedIncomes.items, queriedOutcomes.items)
            EntitiesList.NotGrouped(items = items)
        } else throw IllegalStateException("both lists: ${queriedIncomes::class.qualifiedName} and ${queriedOutcomes::class.qualifiedName} has to be of the same type (grouped/ungrouped")

    }


    companion object {
        private const val errorText =
            """IWarehouseItemRepository is read-only repository, only query function must be used.
                |To insert/remove/update items use IItemIncomeRepository or IItemOutcomeRepository"""
    }

}


/**
 * helper class to identify WarehouseItem without count information
 */
private data class WarehouseItemID(val container: Container?, val item: Item?)

private fun ItemIncome.toWarehouseItemID(): WarehouseItemID {
    return WarehouseItemID(container = container, item = item?.entity)
}

private fun ItemOutcome.toWarehouseItemID(): WarehouseItemID {
    return WarehouseItemID(container = container, item = item?.entity)
}

private fun WarehouseItemID.toWarehouseItem(count: Long): WarehouseItem {
    return WarehouseItem(
        item = item?.let { EntityCountable(it, count) },
        container = container
    )
}

private fun makeWarehouseList(incomes: List<ItemIncome>, outcomes: List<ItemOutcome>): List<WarehouseItem> {
    return (incomes.asSequence() + outcomes.asSequence()).groupBy(
        keySelector = {
            when (it) {
                is ItemIncome -> it.toWarehouseItemID()
                is ItemOutcome -> it.toWarehouseItemID()
                else -> throw IllegalArgumentException("item $it must be ItemIncome or ItemOutcome")
            }
        },
        valueTransform = {
            when (it) {
                is ItemIncome -> it.item?.count ?: 0L
                is ItemOutcome -> -(it.item?.count ?: 0L)
                else -> throw IllegalArgumentException("item $it must be ItemIncome or ItemOutcome")
            }
        })
        .mapValues { entry ->
            entry.value.fold(0L) { acc, l -> acc + l }
        }
        .map { entry ->
            entry.key.toWarehouseItem(entry.value)
        }
}