package persistence.repository

import domain.entities.ItemIncome
import domain.entities.ItemOutcome
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.Incomes
import test.Outcomes

internal class WarehouseItemRepositoryTest {

    lateinit var incomesRepo: BaseRepository<ItemIncome>
    lateinit var outcomesRepo: BaseRepository<ItemOutcome>
    lateinit var warehouseRepo: IWarehouseItemRepository

    @BeforeEach
    fun setUp() {
        warehouseRepo = WarehouseItemRepository(incomesRepo, outcomesRepo)
        runBlocking {
            incomesRepo.insert(Incomes.income1)
            incomesRepo.insert(Incomes.income2)
            incomesRepo.insert(Incomes.income3)

            outcomesRepo.insert(Outcomes.outcome1)
            outcomesRepo.insert(Outcomes.outcome2)
        }
    }

    @Test
    fun test_query_all() {
        val incomes = runBlocking {
            incomesRepo.query(Specification.QueryAll)
        }

        val outcomes = runBlocking {
            outcomesRepo.query(Specification.QueryAll)
        }
        println()
        println("incomes:")
        incomes.forEach {
            println(it)
        }

        println()
        println("outcomes:")
        outcomes.forEach {
            println(it)
        }

        val a = runBlocking {
            warehouseRepo.query(Specification.QueryAll)
        }

        println()
        println("All items:")
        a.forEach {
            println(it)
        }
    }
}