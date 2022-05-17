package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import org.jetbrains.exposed.sql.Column

/**
 * Base DAO interface operating with [IEntity] objects
 */
interface IBaseDao<T : IEntity<String>> {
    /**
     * Get single Entity by [id]
     */
    suspend fun getByID(id: String): T?

    /**
     * Insert Entity
     */
    suspend fun insert(entity: T)

    /**
     * Update Entity
     */
    suspend fun update(entity: T)

    /**
     * Remove Entity by [id]
     */
    suspend fun removeById(id: String)

    /**
     * Querying Entities using specs
     */
    suspend fun query(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null,
        searchSpec: ISpecification? = null,
        groupingSpec: ISpecification? = null
    ): EntitiesList<T>

    /**
     * Get items count using specs
     */
    suspend fun getItemsCount(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null,
        searchSpec: ISpecification? = null,
        groupingSpec: ISpecification? = null
    ): Long

    suspend fun slice(columnName: String, existedSlices: List<SliceValue<Any>> = listOf()): List<SliceValue<*>>
}

/**
 * Class for storing Slice values (data from a single table column)
 */
data class SliceValue<VALUETYPE>(val name: Any, val value: VALUETYPE?, val column: Column<VALUETYPE?>)

/**
 * Class for storing query result.
 */
sealed class EntitiesList<T> {
    /**
     * Grouped query result
     */
    data class Grouped<T>(val items: List<GroupedItem<T>>) : EntitiesList<T>()

    /**
     * Not grouped query result
     */
    data class NotGrouped<T>(val items: List<T>) : EntitiesList<T>()

    fun isNotEmpty(): Boolean {
        return when(this){
            is Grouped -> items.isNotEmpty()
            is NotGrouped -> items.isNotEmpty()
        }
    }

    companion object {
        fun <T : IEntity<*>> empty(): EntitiesList<T> = NotGrouped(listOf())
    }
}

data class GroupedItem<T>(
    val groupID: GroupID,
    val items: List<T>
)

data class GroupID(
    val categoryName: String,
    val key: Any?,
    val keyName: String? = null
)
