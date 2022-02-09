package ui.screens.datatypes

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.localDI
import strings.Strings
import ui.composable.ScrollableBox
import ui.screens.patterns.ScreenWithFilterSheet
import utils.toLocalizedString

@Composable
fun DataTypesScreen() {

    TypesScreenContent()

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypesScreenContent() {
    val di = localDI()
    var currentSelection by remember { mutableStateOf<TypeOfObjects>(TypeOfObjects.ObjectTypes) }
    ScreenWithFilterSheet(
        mainScreenTitle = {
            ListItem(
                modifier = Modifier.align(Alignment.Center).padding(vertical = 16.dp),
                text = {
                    Text(text = currentSelection.name.toLocalizedString(di), style = MaterialTheme.typography.h3)
                },
                secondaryText = {
                    Text(text = currentSelection.description.toLocalizedString(di))
                }
            )
        },
        isOpened = true,
        isModal = true,
        content = {
            ObjectsTypeDetail(
                typeOfObjects = currentSelection
            )
        },
        filterContent = {
            ObjectTypeSelector(
                selectedObject = currentSelection,
                onObjectTypeSelected = { currentSelection = it })
        }
    )


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ObjectTypeSelector(
    modifier: Modifier = Modifier,
    selectedObject: TypeOfObjects,
    onObjectTypeSelected: (type: TypeOfObjects) -> Unit
) {
    Column(modifier = modifier.fillMaxHeight().selectableGroup()) {
        TypeOfObjects.getTypesList().forEachIndexed { index, typeOfObjects ->


            val isSelected = remember(selectedObject, typeOfObjects) {
                typeOfObjects == selectedObject
            }

            val background = when (isSelected) {
                true -> MaterialTheme.colors.primary
                false -> MaterialTheme.colors.surface
            }

            Text(
                modifier = Modifier.fillMaxWidth()

                    .selectable(
                        selected = selectedObject == typeOfObjects,
                        onClick = { onObjectTypeSelected(typeOfObjects) },
                        role = Role.RadioButton
                    )
                    .background(color = background)
                    .padding(8.dp),
                text = typeOfObjects.name.toLocalizedString(),
                color = MaterialTheme.colors.contentColorFor(background)
            )

            if (index != TypeOfObjects.getTypesList().lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }
    }
}

@Composable
private fun ObjectsTypeDetail(
    modifier: Modifier = Modifier,
    typeOfObjects: TypeOfObjects
) {

    val showAddDialog = remember { mutableStateOf(false) }

    val scrollState = remember(typeOfObjects) { ScrollState(initial = 0) }
    ScrollableBox(
        modifier = modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp,
        content = {
            when (typeOfObjects) {
                TypeOfObjects.ObjectTypes -> ObjectTypesScreen(showAddDialog)
                TypeOfObjects.ObjectParameters -> ObjectParametersScreen(showAddDialog)
                TypeOfObjects.Units -> UnitsScreen(showAddDialog)
                TypeOfObjects.Items -> ItemsScreen(showAddDialog)
                TypeOfObjects.Containers -> ContainersScreen(showAddDialog)
            }
        }
    )
//    ScrollableListWithAddButton(
//        modifier = modifier,
//        onAddClick = { showAddDialog.value = true },
//        scrollState = remember(typeOfObjects) { ScrollState(initial = 0) },
//        content = {
//            when (typeOfObjects) {
//                TypeOfObjects.ObjectTypes -> ObjectTypesScreen(showAddDialog)
//                TypeOfObjects.ObjectParameters -> ObjectParametersScreen(showAddDialog)
//                TypeOfObjects.Units -> UnitsScreen(showAddDialog)
//            }
//        }
//    )

}

sealed class TypeOfObjects(val name: Strings, val description: Strings) {
    object ObjectTypes : TypeOfObjects(
        name = Strings.TypesOfData.types_title,
        description = Strings.TypesOfData.types_description
    )

    object ObjectParameters : TypeOfObjects(
        name = Strings.TypesOfData.parameters_title,
        description = Strings.TypesOfData.parameters_description
    )

    object Units : TypeOfObjects(
        name = Strings.TypesOfData.units_title,
        description = Strings.TypesOfData.units_description
    )

    object Items : TypeOfObjects(
        name = Strings.TypesOfData.items_title,
        description = Strings.TypesOfData.items_description
    )

    object Containers : TypeOfObjects(
        name = Strings.TypesOfData.containers_title,
        description = Strings.TypesOfData.containers_description
    )

    companion object {
        fun getTypesList() = listOf(ObjectParameters, ObjectTypes, Units, Items, Containers)
    }

//    override fun equals(other: Any?): Boolean {
//        return if (other is TypeOfObjects) {
//            other.name == name && other.description == description
//        } else
//            super.equals(other)
//    }

}

