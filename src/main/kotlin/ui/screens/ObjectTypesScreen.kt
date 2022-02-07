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
import com.akhris.domain.core.utils.unpack
import domain.application.GetObjectTypes
import domain.entities.ObjectType
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import viewmodels.SingleEntityViewModel

@Composable
fun ObjectTypesScreen() {

    var showTypeEditDialog by remember { mutableStateOf<ObjectType?>(null) }

    val di = localDI()

    val getEntities: GetObjectTypes by di.instance()

    val viewModel: SingleEntityViewModel<String, ObjectType> by di.instance()

    var entities by remember { mutableStateOf(listOf<ObjectType>()) }

    LaunchedEffect(getEntities) {
        entities = getEntities(GetObjectTypes.Params.GetAll).unpack()
    }

    EntityScreenContent(entities)
//
//    ObjectTypesScreenContent(
//        listOf(ResistorTestEntities.resistorsType, CapacitorTestEntities.capacitorsType),
//        onEditType = {
//                     showTypeEditDialog = it
//        },
//        onDeleteType = {})

    showTypeEditDialog?.let { t ->

        EditEntityDialog("edit object type: ${t.name}", t, onSaveClicked = {

        }, onDismiss = { showTypeEditDialog = null })
    }
}

@Composable
fun ObjectTypesScreenContent(
    items: List<ObjectType>,
    onEditType: (ObjectType) -> Unit,
    onDeleteType: (ObjectType) -> Unit
) {
    Column {
        items.forEach {
            ObjectTypeItem(it, onEditType, onDeleteType)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ObjectTypeItem(objectType: ObjectType, onEditType: (ObjectType) -> Unit, onDeleteType: (ObjectType) -> Unit) {

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
                        onEditType(objectType)
                    }, content = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit button",
                            tint = MaterialTheme.colors.secondary
                        )
                    })
                    IconButton(onClick = {
                        onDeleteType(objectType)
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
