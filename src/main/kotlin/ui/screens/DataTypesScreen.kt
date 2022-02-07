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
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.IDUtils
import domain.application.InsertObjectType
import domain.entities.ObjectType
import domain.entities.Parameter
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.composable.ScrollableBox

@Composable
fun DataTypesScreen() {

    val di = localDI()

    val objectInsert: InsertObjectType by di.instance()

    var addNewData by remember { mutableStateOf<IEntity<*>?>(null) }

    TypesScreenContent(onAddNewEntity = {
        addNewData = it
    })

    LaunchedEffect(addNewData) {
        addNewData?.let { entity ->
            when (entity) {
                is ObjectType -> {
                    objectInsert(InsertEntity.Update(entity))
                }
            }
            addNewData = null
        }
    }


}

@Composable
fun TypesScreenContent(
    onAddNewEntity: ((IEntity<*>) -> Unit)? = null
) {
    var addNewEntity by remember { mutableStateOf<IEntity<*>?>(null) }

    var currentSelection by remember { mutableStateOf<TypeOfObjects>(TypeOfObjects.ObjectTypes) }

    Row {
        ObjectTypeSelector(
            modifier = Modifier.weight(3f),
            selectedObject = currentSelection,
            onObjectTypeSelected = { currentSelection = it })

        ObjectsTypeDetail(
            modifier = Modifier.weight(10f),
            typeOfObjects = currentSelection,
            onAddNewType = {
                addNewEntity = when (it) {
                    TypeOfObjects.ObjectParameters -> Parameter(id = IDUtils.newID())
                    TypeOfObjects.ObjectTypes -> ObjectType(id = IDUtils.newID())
                    TypeOfObjects.Units -> domain.entities.Unit(id = IDUtils.newID())
                }
            }
        )
    }

    addNewEntity?.let { entity: IEntity<*> ->
        EditEntityDialog(
            title = "Add new ${entity::class.simpleName}",
            entity = entity,
            onSaveClicked = { onAddNewEntity?.invoke(it) },
            onDismiss = {
                addNewEntity = null
            }
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
private fun ObjectsTypeDetail(
    modifier: Modifier = Modifier,
    typeOfObjects: TypeOfObjects,
    onAddNewType: ((type: TypeOfObjects) -> Unit)? = null
) {

    Box(modifier = modifier) {
        val scrollState = remember(typeOfObjects) { ScrollState(initial = 0) }

        ScrollableBox(modifier = modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp) {
            when (typeOfObjects) {
                TypeOfObjects.ObjectTypes -> ObjectTypesScreen()
                TypeOfObjects.ObjectParameters -> ObjectParametersScreen()
                TypeOfObjects.Units -> UnitsScreen()
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .alpha(
                    if (scrollState.isScrollInProgress) 0.25f else 1f
                ),
            onClick = {
                onAddNewType?.invoke(typeOfObjects)
            },
            content = {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add object type")
            }
        )
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

