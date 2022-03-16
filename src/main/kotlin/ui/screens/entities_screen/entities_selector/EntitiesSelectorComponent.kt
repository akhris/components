package ui.screens.entities_screen.entities_selector

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import ui.composable.ItemRepresentationType
import kotlin.reflect.KClass

class EntitiesSelectorComponent(
    entityClasses: List<KClass<out IEntity<*>>>,
    componentContext: ComponentContext,
    private val onEntitySelected: ((KClass<out IEntity<*>>) -> Unit)? = null
) :
    IEntitiesSelector,
    ComponentContext by componentContext {

    private val _state = MutableValue(
        IEntitiesSelector.Model(
            entitiesSelector = if (entityClasses.size > 1) {
                IEntitiesSelector.EntitiesSelector(selection = entityClasses[0], items = entityClasses)
            } else null
        )
    )

    override val state: Value<IEntitiesSelector.Model> = _state

    override fun selectEntity(entityClass: KClass<out IEntity<*>>) {
        _state.reduce {
            it.copy(entitiesSelector = it.entitiesSelector?.copy(selection = entityClass))
        }
        onEntitySelected?.invoke(entityClass)
    }

    override fun changeItemRepresentationType(itemRepresentationType: ItemRepresentationType) {
        _state.reduce {
            it.copy(itemRepresentationType = itemRepresentationType)
        }
    }
}


//    override fun setFilter(filterSettings: IEntitiesSelector.FilterSettings) {
//        _state.reduce { model ->
//            model.copy(
//                filters =
//                model.filters.replace(filterSettings.copy(isActive = true)) { fs ->
//                    fs.fieldID == filterSettings.fieldID
//                }
//            )
//        }
//    }
//
//    override fun removeFilter(filterSettings: IEntitiesSelector.FilterSettings) {
//        _state.reduce { model ->
//            val filterPresented =
//                model.filters.find { it.fieldID == filterSettings.fieldID } ?: IEntitiesSelector.FilterSettings(
//                    filterSettings.fieldID
//                )
//
//            model.copy(
//                filters = model.filters.replace(filterPresented.copy(isActive = false)) { fs ->
//                    fs.fieldID == filterSettings.fieldID
//                }
//            )
//        }
//    }