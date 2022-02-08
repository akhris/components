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
import ui.screens.patterns.ScreenWithFilterSheet
import ui.screens.patterns.ScrollableListWithAddButton

@Composable
fun DataTypesScreen() {

    TypesScreenContent()

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypesScreenContent() {
    var currentSelection by remember { mutableStateOf<TypeOfObjects>(TypeOfObjects.ObjectTypes) }
    ScreenWithFilterSheet(
        mainScreenTitle = {
            ListItem(
                modifier = Modifier.align(Alignment.Center).padding(vertical = 16.dp),
                text = {
                    Text(text = currentSelection.name, style = MaterialTheme.typography.h3)
                },
                secondaryText = {
                    Text(text = currentSelection.description)
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
        listOf(
            TypeOfObjects.ObjectTypes,
            TypeOfObjects.ObjectParameters,
            TypeOfObjects.Units
        ).forEachIndexed { index, typeOfObjects ->


            val isSelected = remember(selectedObject, typeOfObjects) {
                typeOfObjects == selectedObject
            }

            val background = when (isSelected) {
                true -> MaterialTheme.colors.primary
                false -> MaterialTheme.colors.surface
            }

            Text(modifier = Modifier.fillMaxWidth()

                .selectable(
                    selected = selectedObject == typeOfObjects,
                    onClick = { onObjectTypeSelected(typeOfObjects) },
                    role = Role.RadioButton
                )
                .background(color = background)
                .padding(8.dp),
                text = typeOfObjects.name,
                color = MaterialTheme.colors.contentColorFor(background))

//            ListItem(
//                modifier = Modifier.fillMaxWidth()
//
//                    .selectable(
//                        selected = selectedObject == typeOfObjects,
//                        onClick = { onObjectTypeSelected(typeOfObjects) },
//                        role = Role.RadioButton
//                    )
//                    .background(color = background)
//                    .padding(8.dp),
//                text = {
//                    Text(
//                        text = typeOfObjects.name,
//                        color = MaterialTheme.colors.contentColorFor(background)
//                    )
//                },
//                secondaryText = {
//                    Text(
//                        text = typeOfObjects.description,
//                        color = MaterialTheme.colors.contentColorFor(background).copy(alpha = 0.7f)
//                    )
//                })
            if (index != TypeOfObjects.typesList.lastIndex) {
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
    ScrollableListWithAddButton(
        modifier = modifier,
        onAddClick = { showAddDialog.value = true },
        scrollState = remember(typeOfObjects) { ScrollState(initial = 0) },
        content = {
            when (typeOfObjects) {
                TypeOfObjects.ObjectTypes -> ObjectTypesScreen(showAddDialog)
                TypeOfObjects.ObjectParameters -> ObjectParametersScreen(showAddDialog)
                TypeOfObjects.Units -> UnitsScreen(showAddDialog)
            }
        }
    )
//    Box(modifier = modifier) {
//        val scrollState = remember(typeOfObjects) { ScrollState(initial = 0) }
//
//        ScrollableBox(modifier = modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp) {
//            when (typeOfObjects) {
//                TypeOfObjects.ObjectTypes -> ObjectTypesScreen(showAddDialog)
//                TypeOfObjects.ObjectParameters -> ObjectParametersScreen(showAddDialog)
//                TypeOfObjects.Units -> UnitsScreen(showAddDialog)
//            }
//        }
//
//        FloatingActionButton(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(8.dp)
//                .alpha(
//                    if (scrollState.isScrollInProgress) 0.25f else 1f
//                ),
//            onClick = {
//                showAddDialog.value = true
////                onAddNewType?.invoke(typeOfObjects)
//            },
//            content = {
//                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add object type")
//            }
//        )
//    }
}

sealed class TypeOfObjects(val name: String, val description: String) {
    object ObjectTypes : TypeOfObjects(
        name = "object types",
        description = "Types of objects that are stored in database (resistors, capacitors, ICs,...) and their parameters sets"
    )

    object ObjectParameters : TypeOfObjects(
        name = "object parameters",
        description = "Parameters that objects can have (resistivity, maximum applied voltage, weight, size,...)"
    )

    object Units : TypeOfObjects(
        name = "units",
        description = "Units that object parameters values can have (volts, amps, meters,...)"
    )

    companion object {
        val typesList = listOf(ObjectParameters, ObjectTypes, Units)
    }

//    override fun equals(other: Any?): Boolean {
//        return if (other is TypeOfObjects) {
//            other.name == name && other.description == description
//        } else
//            super.equals(other)
//    }

}

