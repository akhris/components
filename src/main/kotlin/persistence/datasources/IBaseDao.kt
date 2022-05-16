package persistence.datasources

import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.ISpecification
import org.jetbrains.exposed.sql.Column

interface IBaseDao<T : IEntity<*>> {
    suspend fun getByID(id: String): T?

    //    suspend fun getAll(filters: List<FilterSpec>): List<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun removeById(id: String)

    suspend fun query(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null,
        searchSpec: ISpecification? = null,
        groupingSpec: ISpecification? = null
    ): EntitiesList<T>

    suspend fun getItemsCount(
        filterSpec: ISpecification? = null,
        sortingSpec: ISpecification? = null,
        pagingSpec: ISpecification? = null,
        searchSpec: ISpecification? = null,
        groupingSpec: ISpecification? = null
    ): Long

    suspend fun slice(columnName: String, existedSlices: List<SliceValue<Any>> = listOf()): List<SliceValue<*>>
}

data class SliceValue<VALUETYPE>(val name: Any, val value: VALUETYPE?, val column: Column<VALUETYPE?>)

sealed class EntitiesList<T> {
    data class Grouped<T>(val items: List<GroupedItem<T>>) : EntitiesList<T>()
    data class NotGrouped<T>(val items: List<T>) : EntitiesList<T>()
    companion object {
        fun <T : IEntity<*>> empty(): EntitiesList<T> = NotGrouped(listOf())
    }
    fun isNotEmpty(): Boolean {
        return when(this){
            is Grouped -> items.isNotEmpty()
            is NotGrouped -> items.isNotEmpty()
        }
    }
}

data class GroupID(
    val categoryName: String,
    val key: Any?,
    val keyName: String? = null
)

data class GroupedItem<T>(
    val groupID: GroupID,
    val items: List<T>
)