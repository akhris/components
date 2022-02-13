package ui.screens.types_of_data.data_types_list

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.unpack
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.entities.*
import domain.entities.Unit
import domain.entities.usecase_factories.IGetListUseCaseFactory
import kotlinx.coroutines.*
import persistence.repository.Specification
import ui.screens.types_of_data.types_selector.ITypesSelector
import kotlin.reflect.KClass

class DataTypesListComponent(
    getListUseCaseFactory: IGetListUseCaseFactory,
    type: ITypesSelector.Type,
    componentContext: ComponentContext,
    representationType: Value<ITypesSelector.ItemRepresentationType>
) :
    IDataTypesList,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableValue(IDataTypesList.State(type, listOf(), ITypesSelector.ItemRepresentationType.Card))

    override val state: Value<IDataTypesList.State> = _state


    override fun onEntityRemoved(entity: IEntity<*>) {

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
//
//        val useCase = getListUseCaseFactory.getListUseCase(entityClass)
//
//        loadEntities(useCase)
    }

    private fun loadEntities(useCase: GetEntities<String, *>) {
        scope.launch {
            val entities = useCase(GetEntities.GetBySpecification(Specification.QueryAll)).unpack()
            _state.reduce {
                it.copy(entities = entities)
            }
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
            val useCase = getListUseCaseFactory.getListUseCase(it)
            loadEntities(useCase)
        }


    }


}