package viewmodels

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepositoryCallback
import com.akhris.domain.core.repository.RepoResult
import com.akhris.domain.core.utils.unpack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Class that operates with single entity and observe it's changes.
 * To start observing changes need to call [getEntity]
 */
class MultipleEntitiesViewModel<ID, T : IEntity<ID>>(
    private val repoCallback: IRepositoryCallback<T>,
    private val coroutineScope: CoroutineScope,
    private val getEntity: GetEntity<ID, T>,
    private val insertEntity: InsertEntity<ID, T>,
    private val removeEntity: RemoveEntity<ID, T>,
    private val updateEntity: UpdateEntity<ID, T>
) {
    private val _entities: MutableStateFlow<List<T>> = MutableStateFlow(listOf())

//    val type: StateFlow<ObjectType?> = _type

    private suspend fun initEntity(id: ID) {
//        if (_entity.value?.id != id) {
//            val type = getEntity(GetEntity.GetByID(id)).unpack()
//            type?.let {
//                _entity.value = it
//            }
//        }
    }

    fun getEntity(id: ID): StateFlow<List<T>> {
        coroutineScope.launch {
            initEntity(id)
        }
        return _entities
    }

    fun insertEntity(type: T) {
        coroutineScope.launch {
            updateEntity(UpdateEntity.Update(type))
        }
    }

    fun updateEntity(type: T) {
        coroutineScope.launch {
            insertEntity(InsertEntity.Update(type))
        }
    }

    fun removeEntity(type: T) {
        coroutineScope.launch {
            removeEntity(RemoveEntity.Remove(type))
        }
    }

    init {
        coroutineScope.launch {
            repoCallback
                .updates
                .collect { item ->
                    when (item) {
                        is RepoResult.ItemUpdated, is RepoResult.ItemInserted -> {
//                            if (item.item.id == _entity.value?.id) {
//                                _entity.value = item.item
//                            }
                        }
                        is RepoResult.ItemRemoved -> {
//                            if (item.item.id == _entity.value?.id) {
//                                _entity.value = null
//                            }
                        }
                    }
                }
        }
    }


}