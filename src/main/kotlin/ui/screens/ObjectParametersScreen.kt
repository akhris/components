package ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import domain.entities.Parameter
import test.Parameters

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectParametersScreen() {

    var showParameterEditDialog by remember { mutableStateOf<Parameter?>(null) }

    EntityScreenContent(
        entities = listOf(
            Parameters.Electronic.dielectricType,
            Parameters.Electronic.maxVoltage,
            Parameters.Electronic.packg,
            Parameters.Electronic.capacitance,
            Parameters.Electronic.resistance,
            Parameters.Electronic.dielectricType,
            Parameters.Electronic.tolerance,
            Parameters.Material.length,
            Parameters.Material.weight
        )
//        onEditParameter = {
//            showParameterEditDialog = it
//        }
    )

    showParameterEditDialog?.let { p ->

        EditEntityDialog("edit parameter: ${p.name}", p, onSaveClicked = {

        }, onDismiss = { showParameterEditDialog = null })
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectParametersScreenContent(parameters: List<Parameter>, onEditParameter: (Parameter) -> Unit) {
    Column {
        parameters.forEach {
            ObjectParametersScreenItem(it, onEditParameter)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun ObjectParametersScreenItem(parameter: Parameter, onEditParameter: (Parameter) -> Unit) {
    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(8.dp).pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                })
    ) {
        ListItem(
            secondaryText = {
                Text(text = parameter.description)

            },
            text = {
                val text = StringBuilder(parameter.name)
                parameter.unit?.let {
                    text.append(", ")
                    text.append(it.unit)
                }
                Text(text = text.toString())
            },
            trailing = if (isHovered) {
                {
                    IconButton(onClick = {
                        onEditParameter(parameter)
                    }, content = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit button",
                            tint = MaterialTheme.colors.secondary
                        )
                    })
                }
            } else null
        )
    }
}