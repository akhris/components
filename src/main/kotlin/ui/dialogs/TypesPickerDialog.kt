package ui.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import strings.LocalizedStrings
import strings.StringsIDs
import strings.defaultLocalizedStrings


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypesPickerDialog(
    onItemPicked: (Type) -> kotlin.Unit,
    onDismiss: () -> kotlin.Unit,
    localizedStrings: LocalizedStrings = defaultLocalizedStrings
) {
    val types = Type.getAllTypes()

    ListPickerDialog(
        items = types,
        title = "pick data type",
        mapper = { type ->
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(text = type.name?.let { localizedStrings(it) } ?: "")
                }, secondaryText = {
                    Text(text = type.description?.let { localizedStrings(it) } ?: "")
                }
            )
        },
        onItemPicked = onItemPicked,
        onDismiss = onDismiss

    )
}

@Parcelize
sealed class Type(val name: StringsIDs?, val description: StringsIDs?) : Parcelable {
    @Parcelize
    object ObjectType : Type(StringsIDs.types_title, StringsIDs.types_description)

    @Parcelize
    object Parameters : Type(StringsIDs.parameters_title, StringsIDs.parameters_description)

    @Parcelize
    object Units : Type(StringsIDs.units_title, StringsIDs.units_description)

    @Parcelize
    object Items : Type(StringsIDs.items_title, StringsIDs.items_description)

    @Parcelize
    object Containers : Type(StringsIDs.containers_title, StringsIDs.containers_description)

    @Parcelize
    object Suppliers : Type(StringsIDs.suppliers_title, StringsIDs.suppliers_description)

    @Parcelize
    object Invoices : Type(StringsIDs.invoice_title, StringsIDs.invoice_description)

    @Parcelize
    object None : Type(null, null)

    companion object {
        fun getAllTypes() = listOf(ObjectType, Parameters, Units, Items, Containers, Suppliers, Invoices)
        fun getDefaultHomeType(): Type = ObjectType
    }
}

