package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Item
import domain.repository.IItemsRepository
import kotlinx.coroutines.CoroutineDispatcher

typealias GetItem = GetEntity<String, Item>
typealias InsertItem = InsertEntity<String, Item>
typealias RemoveItem = RemoveEntity<String, Item>
typealias UpdateItem = UpdateEntity<String, Item>

class GetItemsList(
    private val repo: IItemsRepository,
    ioDispatcher: CoroutineDispatcher
) :
    UseCase<List<Item>, GetItemsList.Params>(ioDispatcher) {

    override suspend fun run(params: Params): List<Item> {
        return when (params) {
            is Params.GetItemsByType -> repo.getItemsByType(params.typeID)
        }
    }

    sealed class Params {
        class GetItemsByType(val typeID: String) : Params()
    }
}
