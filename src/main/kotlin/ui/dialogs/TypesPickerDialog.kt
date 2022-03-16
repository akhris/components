package ui.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import strings.Strings
import utils.toLocalizedString


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypesPickerDialog(onItemPicked: (Type) -> kotlin.Unit, onDismiss: () -> kotlin.Unit) {
    val types = Type.getAllTypes()

    ListPickerDialog(
        items = types,
        title = "pick data type",
        mapper = { type ->
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(text = type.name?.toLocalizedString() ?: "")
                }, secondaryText = {
                    Text(text = type.description?.toLocalizedString() ?: "")
                }
            )
        },
        onItemPicked = onItemPicked,
        onDismiss = onDismiss

    )
}
@Parcelize
sealed class Type(val name: Strings?, val description: Strings?) : Parcelable {
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
    object Suppliers : Type(Strings.TypesOfData.suppliers_title, Strings.TypesOfData.suppliers_description)

    @Parcelize
    object None : Type(null, null)

    companion object {
        fun getAllTypes() = listOf(ObjectType, Parameters, Units, Items, Containers, Suppliers)
        fun getDefaultHomeType(): Type = ObjectType
    }
}

