package ui.screens.entities_screen.entities_view_settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

class EntitiesViewSettingsComponent(componentContext: ComponentContext, private val onTypeChanged: (ItemRepresentationType)->Unit) :
    IEntitiesViewSettings, ComponentContext by componentContext {

    private val _state: MutableValue<IEntitiesViewSettings.Model> = MutableValue(IEntitiesViewSettings.Model())

    override val state: Value<IEntitiesViewSettings.Model> = _state

    override fun onRepresentationTypeChanged(newType: ItemRepresentationType) {
        _state.reduce {
            it.copy(currentType = newType)
        }
        onTypeChanged(newType)
    }
}