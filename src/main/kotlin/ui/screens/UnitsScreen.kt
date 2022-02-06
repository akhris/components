package ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.entities.Unit
import test.Units

@Composable
fun UnitsScreen() {
    UnitsScreenContent(
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
    )
}

@Composable
fun UnitsScreenContent(units: List<Unit>) {
    Column {
        units.forEach { unit -> UnitsScreenItem(unit) }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UnitsScreenItem(unit: Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(8.dp)
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
            })
    }
}