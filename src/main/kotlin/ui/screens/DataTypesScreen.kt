package ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ui.composable.ScrollableBox

@Composable
fun DataTypesScreen() {
    TypesScreenContent()
}

@Composable
fun TypesScreenContent() {

    var currentSelection by remember { mutableStateOf<TypeOfObjects>(TypeOfObjects.ObjectTypes) }

    Row {
        ObjectTypeSelector(
            modifier = Modifier.weight(3f),
            selectedObject = currentSelection,
            onObjectTypeSelected = { currentSelection = it })

        ObjectsTypeDetail(
            modifier = Modifier.weight(10f),
            typeOfObjects = currentSelection
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ObjectTypeSelector(
    modifier: Modifier = Modifier,
    selectedObject: TypeOfObjects,
    onObjectTypeSelected: (type: TypeOfObjects) -> Unit
) {
    println("selectedObject: $selectedObject")
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


            ListItem(
                modifier = Modifier.fillMaxWidth()

                    .selectable(
                        selected = selectedObject == typeOfObjects,
                        onClick = { onObjectTypeSelected(typeOfObjects) },
                        role = Role.RadioButton
                    )
                    .background(color = background)
                    .padding(8.dp),
                text = {
                    Text(
                        text = typeOfObjects.name,
                        color = MaterialTheme.colors.contentColorFor(background)
                    )
                },
                secondaryText = {
                    Text(
                        text = typeOfObjects.description,
                        color = MaterialTheme.colors.contentColorFor(background).copy(alpha = 0.7f)
                    )
                })
            if (index != TypeOfObjects.typesList.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }
    }
}

@Composable
private fun ObjectsTypeDetail(modifier: Modifier = Modifier, typeOfObjects: TypeOfObjects) {

    Box(modifier = modifier) {
        val scrollState = remember(typeOfObjects) { ScrollState(initial = 0) }

        ScrollableBox(modifier = modifier.fillMaxHeight(), scrollState = scrollState) {
            when (typeOfObjects) {
                TypeOfObjects.ObjectTypes -> ObjectTypesScreen()
                TypeOfObjects.ObjectParameters -> ObjectParametersScreen()
                TypeOfObjects.Units -> UnitsScreen()
            }
        }
        println("scroll in progress: ${scrollState.isScrollInProgress}")

        FloatingActionButton(modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).alpha(
            if (scrollState.isScrollInProgress) 0.25f else 1f
        ), onClick = {
            //on add object type clicked
        }, content = {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add object type")
        })
    }
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

