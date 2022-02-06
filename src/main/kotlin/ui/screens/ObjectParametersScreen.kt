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
import domain.entities.Parameter
import test.Parameters

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectParametersScreen() {
    ObjectParametersScreenContent(
        parameters = listOf(
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
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectParametersScreenContent(parameters: List<Parameter>) {
    Column {
        parameters.forEach {
            ObjectParametersScreenItem(it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectParametersScreenItem(parameter: Parameter) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(8.dp)
    ) {
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