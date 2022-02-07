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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Window
import domain.entities.Unit
import test.Units

@Composable
fun UnitsScreen() {
    var showUnitEditDialog by remember { mutableStateOf<Unit?>(null) }

    EntityScreenContent(
        listOf(
            Units.Electronic.amps,
            Units.Electronic.volts,
            Units.Electronic.farads,
            Units.Electronic.ohm,
            Units.Electronic.watt,
            Units.Common.grams,
            Units.Common.meters,
            Units.Common.pcs,
            Units.Common.percent
        )
//        onEditUnit = {
//            showUnitEditDialog = it
//        }
    )

    showUnitEditDialog?.let { u ->

        EditEntityDialog("edit unit: ${u.unit}", u, onSaveClicked = {}, onDismiss = { showUnitEditDialog = null })
    }

//    showUnitEditDialog?.let { u ->
//
////        Dialog(onCloseRequest = { showUnitEditDialog = null }, content = {
////            EditEntityScreen(u, onSaveClicked = { changedUnit ->
////                showUnitEditDialog = null
////            })
////        }, title = "edit unit: ${u.unit}")
//    }
}

@Composable
fun UnitsScreenContent(units: List<Unit>, onEditUnit: (Unit) -> kotlin.Unit) {
    Column {
        units.forEach { unit -> UnitsScreenItem(unit, onEditUnit) }
    }
}

@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun UnitsScreenItem(unit: Unit, onEditUnit: (Unit) -> kotlin.Unit) {

    var isHovered by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp).pointerMoveFilter(
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
            text = { Text(text = unit.unit) },
            secondaryText = {
                Text(
                    text = if (unit.isMultipliable) {
                        "multipliable"
                    } else {
                        "not multipliable"
                    }
                )
            }, trailing = if (isHovered) {
                {
                    IconButton(onClick = {
                        onEditUnit(unit)
                    }, content = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit button",
                            tint = MaterialTheme.colors.secondary
                        )
                    })
                }
            } else null)
    }
}