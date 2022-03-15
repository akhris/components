package ui.screens.entities_screen.entities_sidepanel

import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import ui.screens.types_of_data.types_selector.ItemRepresentationType
import kotlin.reflect.KClass

class EntitiesSidePanelComponent(
    entityClasses: List<KClass<out IEntity<*>>>,
    componentContext: ComponentContext,
    private val onEntitySelected: ((KClass<out IEntity<*>>) -> Unit)? = null
) :
    IEntitiesSidePanel,
    ComponentContext by componentContext {

    private val _state = MutableValue(
        IEntitiesSidePanel.Model(
            entitiesSelector = if (entityClasses.size > 1) {
                IEntitiesSidePanel.EntitiesSelector(selection = entityClasses[0], items = entityClasses)
            } else null
        )
    )

    override val state: Value<IEntitiesSidePanel.Model> = _state

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

    override fun setFilter(filterSettings: IEntitiesSidePanel.FilterSettings) {
        TODO("Not yet implemented")
    }

    override fun removeFilter(filterSettings: IEntitiesSidePanel.FilterSettings) {
        TODO("Not yet implemented")
    }
}