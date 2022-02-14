package ui.screens.entity_renderers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderBooleanFieldReadOnly(field: EntityField.BooleanField) {
    Row {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(
                    text = field.fieldID.name
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
fun RenderIsFavoriteFieldReadOnly(field: EntityField.FavoriteField) {
    RenderBooleanFieldReadOnly(
        EntityField.BooleanField(
            fieldID = field.fieldID,
            value = field.isFavorite,
            description = field.description
        )
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderFloatFieldReadOnly(field: EntityField.FloatField) {
    ListItem(
        text = { Text(text = field.value.toString(), style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.fieldID.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderUrlFieldReadOnly(field: EntityField.URLField) {
    ListItem(
        icon = { Icon(painter = painterResource("vector/language_black_24dp.svg"), contentDescription = "open url") },
        text = { Text(text = field.url.toString(), style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.fieldID.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderTextFieldReadOnly(field: EntityField.StringField) {
    ListItem(
        text = { Text(text = field.value, style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.fieldID.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderCaptionFieldReadOnly(field: EntityField.CaptionField) {
    ListItem(
        text = { Text(text = field.caption, style = MaterialTheme.typography.caption) },
        secondaryText = { Text(text = field.description) },
        overlineText = { Text(text = field.fieldID.name) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinkReadOnly(field: EntityField.EntityLink) {
    if (field.entity != null) {

        ListItem(
            text = {
                Text(
                    text = field.getName()
                )
            },
            secondaryText = { Text(text = field.description) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksListReadOnly(field: EntityField.EntityLinksList) {
    Column {
        ListItem(
            text = { Text(text = field.fieldID.name) },
            secondaryText = { Text(text = field.description) }
        )

        field.entities.forEach {
            RenderEntityLinkReadOnly(it)
        }
    }
}

