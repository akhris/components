package ui.screens.types_of_data.types_selector

import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import strings.Strings

interface ITypesSelector {

    val models: Value<Model>

    fun onTypeClicked(type: Type)

    data class Model(
        val types: List<Type>,
        val selectedType: Type?
    )

    @Parcelize
    sealed class Type(val name: Strings?, val description: Strings?):Parcelable {
        @Parcelize
        object ObjectType : Type(Strings.TypesOfData.types_title, Strings.TypesOfData.types_description)

        @Parcelize
        object Parameters : Type(Strings.TypesOfData.parameters_title, Strings.TypesOfData.parameters_description)

        @Parcelize
        object Units : Type(Strings.TypesOfData.units_title, Strings.TypesOfData.units_description)

        @Parcelize
        object Items : Type(Strings.TypesOfData.items_title, Strings.TypesOfData.items_description)

        @Parcelize
        object Containers : Type(Strings.TypesOfData.containers_title, Strings.TypesOfData.containers_description)

        @Parcelize
        object None : Type(null, null)

        companion object {
            fun getAllTypes() = listOf(ObjectType, Parameters, Units, Items, Containers)
            fun getDefaultHomeType(): Type = ObjectType
        }
    }
}
