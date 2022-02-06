package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import domain.entities.ObjectType
import test.CapacitorTestEntities
import test.ResistorTestEntities

@Composable
fun ObjectTypesScreen() {
    ObjectTypesScreenContent(listOf(ResistorTestEntities.resistorsType, CapacitorTestEntities.capacitorsType))
}

@Composable
fun ObjectTypesScreenContent(items: List<ObjectType>) {
    Column {
        items.forEach {
            ObjectTypeItem(it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ObjectTypeItem(objectType: ObjectType) {

    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                })
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            //title:
            Row(modifier = Modifier.fillMaxWidth().height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = objectType.name,
                    style = MaterialTheme.typography.h6
                )
                if (isHovered) {
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        //on edit clicked
                    }, content = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit button",
                            tint = MaterialTheme.colors.secondary
                        )
                    })
                    IconButton(onClick = {
                        //on delete clicked
                    }, content = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete button",
                            tint = MaterialTheme.colors.error
                        )
                    })
                }
            }
            //params:
            Column {
                objectType.parameters.forEach { parameter ->
                    ListItem(
                        secondaryText = { Text(text = parameter.description) },
                        text = { Text(text = parameter.name) },
                        trailing = parameter.unit?.let {
                            {
                                Text(text = it.unit)
                            }
                        })
                }
            }
        }
    }
}
