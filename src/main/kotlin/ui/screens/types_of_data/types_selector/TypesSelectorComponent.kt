package ui.screens.types_of_data.types_selector

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce

class TypesSelectorComponent(
    componentContext: ComponentContext,
    selectedType: ITypesSelector.Type? = null,
    private val onTypeSelected: (ITypesSelector.Type) -> Unit,
    private val onRepresentationTypeChanged: (ItemRepresentationType) -> Unit,

    ) :
    ITypesSelector, ComponentContext by componentContext {

    private val _models = MutableValue(
        ITypesSelector.Model(
            itemRepresentationType = ItemRepresentationType.Card,
            types = ITypesSelector.Type.getAllTypes(),
            selectedType = selectedType
        )
    )

    override val models: Value<ITypesSelector.Model> = _models

    override fun onTypeClicked(type: ITypesSelector.Type) {
        onTypeSelected(type)
        _models.reduce { it.copy(selectedType = type) }
    }

    override fun onItemRepresentationTypeChanged(type: ItemRepresentationType) {
        _models.reduce { it.copy(itemRepresentationType = type) }
        onRepresentationTypeChanged(type)
    }

}