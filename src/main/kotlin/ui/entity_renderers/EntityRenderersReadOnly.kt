package ui.entity_renderers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName
import utils.DateTimeConverter

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
fun RenderFloatFieldReadOnly(field: EntityField.FloatField) {
    ListItem(
        text = { Text(text = field.value.toString(), style = MaterialTheme.typography.caption) },
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
fun RenderEntityLinkReadOnly(field: EntityField.EntityLink) {
    when(field){
        is EntityField.EntityLink.EntityLinkCountable -> RenderEntityLinkCountable(field)
        is EntityField.EntityLink.EntityLinkSimple -> RenderEntityLinkCommon(field)
        is EntityField.EntityLink.EntityLinkValuable -> RenderEntityLinkValuable(field)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderEntityLinkCountable(field: EntityField.EntityLink.EntityLinkCountable){
    ListItem(
        text = {
            Text(
                text = field.getName()
            )
        },
        secondaryText = { Text(text = field.description) },
        trailing = field.count?.let { c ->
            {
                Text(text = "$c pcs")
            }
        }
    )
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderEntityLinkValuable(field: EntityField.EntityLink.EntityLinkValuable){
    ListItem(
        text = {
            Text(
                text = field.getName()
            )
        },
        secondaryText = { Text(text = field.description) },
        trailing = field.value?.let { v ->
            {
                Text(text = v)
            }
        }
    )
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderEntityLinkCommon(field: EntityField.EntityLink.EntityLinkSimple){
    ListItem(
        text = {
            Text(
                text = field.getName()
            )
        },
        secondaryText = { Text(text = field.description) }

    )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderDateTimeReadOnly(field: EntityField.DateTimeField) {
    ListItem(
        text = { field.value?.let {dateTime-> Text(DateTimeConverter.dateTimeToString(dateTime)) } },
        secondaryText = { Text(text = field.description) }
    )
}