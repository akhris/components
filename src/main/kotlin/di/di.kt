package di

import di.persistence.repository.ItemsTestRepository
import di.persistence.repository.ObjectTypesTestRepository
import domain.application.*
import domain.repository.IItemsRepository
import domain.repository.ITypesRepository
import kotlinx.coroutines.Dispatchers
import org.kodein.di.*

val di = DI {
    bindSingleton<IItemsRepository> { ItemsTestRepository() }
    bindMultiton { repo: IItemsRepository -> GetItem(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: IItemsRepository -> UpdateItem(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: IItemsRepository -> RemoveItem(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: IItemsRepository -> InsertItem(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: IItemsRepository -> GetItemsList(repo, ioDispatcher = Dispatchers.IO) }

    bindSingleton<ITypesRepository> { ObjectTypesTestRepository() }
    bindMultiton { repo: ITypesRepository -> GetObjectType(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: ITypesRepository -> UpdateObjectType(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: ITypesRepository -> RemoveObjectType(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: ITypesRepository -> InsertObjectType(repo, ioDispatcher = Dispatchers.IO) }
    bindMultiton { repo: ITypesRepository -> GetObjectTypes(repo, ioDispatcher = Dispatchers.IO) }
}
