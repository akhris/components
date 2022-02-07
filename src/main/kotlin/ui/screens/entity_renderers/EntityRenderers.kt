package ui.screens.entity_renderers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import domain.entities.fieldsmappers.EntityField

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderBooleanField(field: EntityField.BooleanField, onValueChange: (Boolean) -> Unit) {
    Row {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(
                    text = field.name
                )
            }, secondaryText = {
                Text(text = field.description)
            }, trailing = {
                Switch(field.value, onCheckedChange = onValueChange)
            })


    }
}

@Composable
fun RenderTextField(field: EntityField.StringField, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = field.value,
        label = { Text(text = field.name) },
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
fun RenderCaptionField(field: EntityField.CaptionField) {
    ListItem(
        text = { Text(text = field.caption, style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLink(field: EntityField.EntityLink, onEntityLinkSelect: () -> Unit, onEntityLinkClear: () -> Unit) {
    if (field.entity != null) {
        ListItem(
            modifier = Modifier.clickable {
                onEntityLinkSelect()
            },
            text = { Text(text = field.name) },
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
    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = { onEntityLinkSelect() },
                content = { Text("add ${field.name}") })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksList(field: EntityField.EntityLinksList, onEntityLinkAdd: () -> Unit) {
    Column {
        ListItem(
            text = { Text(text = field.name) },
            secondaryText = { Text(text = field.description) }
        )

        field.entities.forEach {
            RenderEntityLink(it, {}, {})
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onEntityLinkAdd,
                content = { Text("add ${field.name}") })
        }

    }

}