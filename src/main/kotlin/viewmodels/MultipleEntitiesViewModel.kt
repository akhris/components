package viewmodels

import com.akhris.domain.core.application.*
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepositoryCallback
import com.akhris.domain.core.repository.RepoResult
import com.akhris.domain.core.utils.unpack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import persistence.repository.Specification
import utils.replace

/**
 * Class that operates with single entity and observe it's changes.
 * To start observing changes need to call [getEntity]
 */
class MultipleEntitiesViewModel<ID, T : IEntity<ID>>(
    private val repoCallback: IRepositoryCallback<T>,
    private val coroutineScope: CoroutineScope,
    private val getEntity: GetEntity<ID, T>,
    private val getEntities: GetEntities<ID, T>,
    private val insertEntity: InsertEntity<ID, T>,
    private val removeEntity: RemoveEntity<ID, T>,
    private val updateEntity: UpdateEntity<ID, T>
) {
    private val _entities: MutableStateFlow<List<T>> = MutableStateFlow(listOf())

    val entities: StateFlow<List<T>> = _entities


    private suspend fun invalidateEntities() {
        val entities = getEntities(GetEntities.GetBySpecification(Specification.QueryAll)).unpack()
        _entities.value = entities
    }


    fun insertEntity(type: T) {
        coroutineScope.launch {
            insertEntity(InsertEntity.Insert(type))
        }
    }

    fun updateEntity(type: T) {
        coroutineScope.launch {
            updateEntity(UpdateEntity.Update(type))
        }
    }

    fun removeEntity(type: T) {
        coroutineScope.launch {
            removeEntity(RemoveEntity.Remove(type))
        }
    }

    init {
        coroutineScope.launch {
            invalidateEntities()
        }
        coroutineScope.launch {
            repoCallback
                .updates
                .collect { item ->
                    val cachedList = _entities.value
                    val cachedItem = cachedList.find { it.id == item.item.id }
                    when (item) {
                        is RepoResult.ItemUpdated -> {
                            //update entities:
                            if (cachedItem != null && cachedItem != item.item) {
                                _entities.value = cachedList.replace(item.item) {
                                    it.id == item.item.id
                                }
                            }
                        }
                        is RepoResult.ItemInserted -> {
                            //insert item in the list (just at the end):
                            //todo apply sorting/filtering here
                            if (cachedItem == null) {
                                _entities.value = cachedList.plus(item.item)
                            }
                        }
                        is RepoResult.ItemRemoved -> {
                            if (cachedItem != null) {
                                _entities.value = cachedList.minus(cachedItem)
                            }
                        }
                    }
                }
        }
    }


}