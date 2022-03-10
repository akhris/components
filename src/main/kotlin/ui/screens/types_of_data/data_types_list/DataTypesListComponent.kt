package ui.screens.types_of_data.data_types_list

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.application.Result
import com.akhris.domain.core.application.UpdateEntity
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.repository.IRepository
import com.akhris.domain.core.utils.log
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.entities.*
import domain.entities.Unit
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IUpdateUseCaseFactory
import kotlinx.coroutines.*
import persistence.repository.Specification
import ui.screens.types_of_data.types_selector.ITypesSelector
import ui.screens.types_of_data.types_selector.ItemRepresentationType
import kotlin.reflect.KClass

class DataTypesListComponent(
    private val updateUseCaseFactory: IUpdateUseCaseFactory,
    private val getListUseCaseFactory: IGetListUseCaseFactory,
    private val type: ITypesSelector.Type,
    componentContext: ComponentContext,
    representationType: Value<ItemRepresentationType>
) :
    IDataTypesList,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableValue(IDataTypesList.State(type, listOf(), ItemRepresentationType.Card))

    override val state: Value<IDataTypesList.State> = _state


    override fun onEntityRemoved(entity: IEntity<*>) {

    }

    override fun onEntityUpdated(entity: IEntity<*>) {
        val typeClass = getTypeClass(type)
        typeClass?.let { entClass ->
//            val repo = repositoryFactory.getRepository(entClass)
//            updateEntities(repo = repo as IRepository<String, IEntity<String>>, entity = entity as IEntity<String>)
            val updateUseCase = updateUseCaseFactory.getUpdateUseCase(entClass)
            updateEntities(
                useCase = updateUseCase as UpdateEntity<String, IEntity<String>>,
                entity = entity as IEntity<String>
            )

        }
    }


    private fun getTypeClass(type: ITypesSelector.Type): KClass<out IEntity<String>>? {
        val entityClass = when (type) {
            ITypesSelector.Type.Containers -> Container::class
            ITypesSelector.Type.Items -> Item::class
            ITypesSelector.Type.ObjectType -> ObjectType::class
            ITypesSelector.Type.Parameters -> Parameter::class
            ITypesSelector.Type.Units -> Unit::class
            ITypesSelector.Type.None -> null
            ITypesSelector.Type.Suppliers -> Supplier::class
        }
        return entityClass
    }


    private fun loadEntities(useCase: GetEntities<*, *>) {
        scope.launch {
            when (val entitiesResult = useCase(GetEntities.GetBySpecification(Specification.QueryAll))) {
                is Result.Success -> {
                    _state.reduce {
                        it.copy(entities = entitiesResult.value)
                    }
                }
                is Result.Failure -> {
                    log(entitiesResult.throwable)
                }
            }

        }
    }

    private fun loadEntities(repo: IRepository<*, *>) {
        scope.launch {
            val entities = repo.query(Specification.QueryAll)

            log("repo: $repo")
            log("loaded entities:")
            log(entities)
            _state.reduce {
                it.copy(entities = entities)
            }
        }
    }

    private fun updateEntities(repo: IRepository<String, IEntity<String>>, entity: IEntity<String>) {
        scope.launch {
            repo.update(entity)
        }
    }

    private fun updateEntities(useCase: UpdateEntity<String, IEntity<String>>, entity: IEntity<String>) {
        scope.launch {
            useCase(UpdateEntity.Update(entity))
        }
    }

    init {
        representationType.subscribe { newType ->
            _state.reduce { it.copy(itemRepresentationType = newType) }
        }

        lifecycle.subscribe(onDestroy = {
            representationType.unsubscribe {

            }
            scope.coroutineContext.cancelChildren()
        })

        val typeClass = getTypeClass(type)
        typeClass?.let {
            val getEntitiesList = getListUseCaseFactory.getListUseCase(it)
            loadEntities(getEntitiesList)
        }


    }


}