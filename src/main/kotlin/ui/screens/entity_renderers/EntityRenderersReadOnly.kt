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
fun RenderBooleanFieldReadOnly(field: EntityField.BooleanField) {
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
                Switch(field.value, onCheckedChange = {})
            })


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderTextFieldReadOnly(field: EntityField.StringField) {
    ListItem(
        text = { Text(text = field.value, style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderCaptionFieldReadOnly(field: EntityField.CaptionField) {
    ListItem(
        text = { Text(text = field.caption, style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinkReadOnly(field: EntityField.EntityLink) {
    if (field.entity != null) {
        ListItem(
            text = { Text(text = field.name) },
            secondaryText = { Text(text = field.description) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksListReadOnly(field: EntityField.EntityLinksList) {
    Column {
        ListItem(
            text = { Text(text = field.name) },
            secondaryText = { Text(text = field.description) }
        )

        field.entities.forEach {
            RenderEntityLinkReadOnly(it)
        }

    }

}