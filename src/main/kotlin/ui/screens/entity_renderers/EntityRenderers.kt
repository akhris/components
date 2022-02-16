package ui.screens.entity_renderers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderBooleanField(field: EntityField.BooleanField, onValueChange: (Boolean) -> Unit) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = field.fieldID.name
            )
        }, secondaryText = {
            Text(text = field.description)
        }, trailing = {
            Checkbox(field.value, onCheckedChange = onValueChange)
        })
}

@Composable
fun RenderFloatField(field: EntityField.FloatField, onValueChange: (Float) -> Unit) {

    var value by remember(field) { mutableStateOf(field.value.toString()) }

    val floatValue = remember(value) { value.toFloatOrNull() }

    TextField(
        isError = floatValue == null,
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = field.fieldID.name) },
        onValueChange = {
            value = it
            floatValue?.let { f ->
                onValueChange(f)
            }
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onValueChange(0f) },
                imageVector = Icons.Rounded.Clear,
                contentDescription = "clear text"
            )
        }
    )
}

@Composable
fun RenderTextField(field: EntityField.StringField, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = field.value,
        label = { Text(text = field.fieldID.name) },
        onValueChange = onValueChange,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onValueChange("") },
                imageVector = Icons.Rounded.Clear,
                contentDescription = "clear text"
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLink(
    field: EntityField.EntityLink<*>,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: () -> Unit
) {
    if (field.entity != null) {
        Column {
            ListItem(
                modifier = Modifier.clickable {
                    onEntityLinkSelect()
                },
                text = { Text(text = field.getName()) },
                secondaryText = { Text(text = field.description) },
                trailing = {
                    Icon(
                        modifier = Modifier.clickable {
                            onEntityLinkClear()
                        },
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "clear entity"
                    )
                }
            )
            field.count?.let {
                OutlinedTextField(
                    modifier = Modifier.align(Alignment.End),
                    value = it.toString(),
                    onValueChange = {},
                    trailingIcon = {
                        Column {
                            Icon(Icons.Rounded.Add, contentDescription = "count up")
                            Icon(
                                painter = painterResource("vector/remove_black_24dp.svg"),
                                contentDescription = "count down"
                            )
                        }
                    }
                )
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = { onEntityLinkSelect() },
                content = { Text("add ${field.fieldID.name}") })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksList(
    field: EntityField.EntityLinksList<*>,
    onEntityLinkAdd: () -> Unit,
    onEntityLinkClear: (EntityField.EntityLink<*>) -> Unit
) {
    Column {
        ListItem(
            text = { Text(text = field.fieldID.name) },
            secondaryText = { Text(text = field.description) }
        )

        field.entities.forEach { link ->
            RenderEntityLink(link, {}, onEntityLinkClear = { onEntityLinkClear(link) })
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onEntityLinkAdd,
                content = { Text("add ${field.fieldID.name}") })
        }

    }

}