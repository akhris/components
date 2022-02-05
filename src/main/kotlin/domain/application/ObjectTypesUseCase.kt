package domain.application

import com.akhris.domain.core.application.*
import domain.entities.ObjectType
import domain.repository.ITypesRepository
import kotlinx.coroutines.CoroutineDispatcher

typealias GetObjectType = GetEntity<String, ObjectType>
typealias InsertObjectType = InsertEntity<String, ObjectType>
typealias RemoveObjectType = RemoveEntity<String, ObjectType>
typealias UpdateObjectType = UpdateEntity<String, ObjectType>

class GetObjectTypes(
    private val repo: ITypesRepository,
    ioDispatcher: CoroutineDispatcher
) : UseCase<List<ObjectType>, GetObjectTypes.Params>(ioDispatcher) {

    override suspend fun run(params: Params): List<ObjectType> {
        return when (params) {
            Params.GetAll -> repo.getAllItemsTypes()
        }
    }

    sealed class Params {
        object GetAll : Params()
    }
}