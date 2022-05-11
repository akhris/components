package persistence.repository

import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.repository.ISpecification
import com.akhris.domain.core.utils.log
import domain.entities.EntityCountable
import domain.entities.ItemIncome
import domain.entities.ItemOutcome
import domain.entities.WarehouseItem
import persistence.datasources.ListItem

/**
 * repository that handles items in warehouse.
 * This repo is dependent on [IItemIncomeRepository] and [IItemOutcomeRepository], it calculates overall items count as
 * incomes minus outcomes.
 * This repo is read-only, so only query function is overriden.
 */
class WarehouseItemRepository(
    private val incomeRepository: IGroupingRepository<String, ItemIncome>,
    private val outcomeRepository: IGroupingRepository<String, ItemOutcome>
) : IWarehouseItemRepository {

    override suspend fun getByID(id: String): WarehouseItem {
        throw UnsupportedOperationException(errorText)
    }

    override suspend fun insert(t: WarehouseItem) {
        throw UnsupportedOperationException(errorText)
    }


    override suspend fun query(specification: ISpecification): List<WarehouseItem> {
        return when (specification) {
            is Specification -> queryList(specification)
            else -> listOf()
        }
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


    private suspend fun queryList(specification: Specification): List<WarehouseItem> {
        //get all incomes by specification
        val incomes =
            kotlin
                .runCatching { incomeRepository.gQuery(specification) }
                .getOrElse { listOf() }
//                .filter {
//                    when (it) {
//                        is ListItem.GroupedItem -> it.
//                        is ListItem.NotGroupedItem -> it.item != null
//                    }
//                    it.item != null
//                }
                .groupBy(keySelector = { it.item?.entity!! },
                    valueTransform = { income -> income.container to (income.item?.count ?: 0L) })


        //get all outcomes by specification
        val outcomes = kotlin.runCatching { outcomeRepository.gQuery(specification) }.getOrElse { listOf() }
            .filter { it.item != null }.groupBy(keySelector = { it.item?.entity!! },
                valueTransform = { income -> income.container to -(income.item?.count ?: 0L) })

        log("got incomes size: ${incomes.size}")
        log("got outcomes size: ${outcomes.size}")

        //merge maps like here: https://stackoverflow.com/questions/54232530/merge-values-in-map-kotlin
        //also count overall presence of item using fold
        val overall =
            (incomes.asSequence() + outcomes.asSequence()).groupBy({ it.key }, { it.value }).mapValues { (_, values) ->
                values.flatten().groupBy(keySelector = { p -> p.first }, valueTransform = { p -> p.second })
                    .mapValues { (_, values) -> values.fold(0L) { a, c -> a + c } }
            }

        //return overall map that is flatten by creating WarehouseItem for each container
        return overall.flatMap { (item, presence) ->
            presence.map { (container, count) ->
                WarehouseItem(item = EntityCountable(item, count), container = container)
            }
        }

    }


    companion object {
        private const val errorText =
            """IWarehouseItemRepository is read-only repository, only query function must be used.
                |To insert/remove/update items use IItemIncomeRepository or IItemOutcomeRepository"""
    }

}

private fun ItemIncome.toWarehouseItem(): WarehouseItem = WarehouseItem(item = this.item, container = this.container)
private fun ItemOutcome.toWarehouseItem(): WarehouseItem = WarehouseItem(item = this.item, container = this.container)