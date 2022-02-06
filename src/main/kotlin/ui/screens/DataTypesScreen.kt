package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            selected = currentSelection,
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
    selected: TypeOfObjects,
    onObjectTypeSelected: (type: TypeOfObjects) -> Unit
) {
    println("ObjectTypeSelector: selected=$selected, typesList=${TypeOfObjects.typesList}")
    Column(modifier = modifier.fillMaxHeight()) {
        listOf(
            TypeOfObjects.ObjectTypes,
            TypeOfObjects.ObjectParameters,
            TypeOfObjects.Units
        ).forEachIndexed { index, typeOfObjects ->
            ListItem(
                modifier = Modifier.fillMaxWidth().selectable(
                    selected = selected == typeOfObjects,
                    onClick = { onObjectTypeSelected(typeOfObjects) }),
                text = { Text(text = typeOfObjects.name) },
                secondaryText = { Text(text = typeOfObjects.description) })
            if (index != TypeOfObjects.typesList.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }
    }
}

@Composable
private fun ObjectsTypeDetail(modifier: Modifier = Modifier, typeOfObjects: TypeOfObjects) {

    Column(modifier = modifier.fillMaxHeight()) {
        when (typeOfObjects) {
            TypeOfObjects.ObjectTypes -> ObjectTypesScreen()
            TypeOfObjects.ObjectParameters -> ObjectParametersScreen()
            TypeOfObjects.Units -> UnitsScreen()
        }
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

}

