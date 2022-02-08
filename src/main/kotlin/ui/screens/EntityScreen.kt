package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.IFieldsMapperFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.entity_renderers.*
import ui.theme.DialogSettings

@Composable
fun EntityScreen() {

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity<*>> EntityScreenContent(
    entities: List<T>,
    onEntityRemoved: ((T) -> Unit)? = null
) {

    Column {
        entities.forEach { entity ->
            RenderEntity(
                entity,
                onEntityChanged = { println("entity changed: $it") },
                onEntityRemoved = onEntityRemoved)
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun <T : IEntity<*>> RenderEntity(
    initialEntity: T,
    onEntityChanged: (T) -> Unit,
    onEntityRemoved: ((T) -> Unit)? = null
) {
    val di = localDI()
    val factory: IFieldsMapperFactory by di.instance()
    val mapper = remember(factory) { factory.getFieldsMapper(initialEntity::class) }
    var showDeletePrompt by remember { mutableStateOf<T?>(null) }

    var entity by remember(initialEntity) { mutableStateOf(initialEntity) }

    val fields = remember(mapper, entity) { mapper.mapFields(entity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            fields.forEach {
                RenderField(it, onFieldChange = { changedField ->
                    entity = mapper.mapIntoEntity(entity, changedField)
//                    onEntityChanged(mapper.mapIntoEntity(initialEntity, changedField))
                })
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                    onClick = { showDeletePrompt = initialEntity },
                    content = { Text(text = "delete", color = MaterialTheme.colors.error) })

                if (entity != initialEntity) {
                    TextButton(
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                        onClick = { entity = initialEntity },
                        content = { Text(text = "discard") })
                    Button(
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                        onClick = { onEntityChanged(entity) },
                        content = { Text(text = "save") })
                }
            }
        }
    }

    showDeletePrompt?.let { e ->
        AlertDialog(onDismissRequest = { showDeletePrompt = null }, buttons = {
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                TextButton(onClick = {
                    showDeletePrompt = null
                }, content = { Text(text = "cancel") })
                Button(onClick = {
                    onEntityRemoved?.invoke(e)
                    showDeletePrompt = null
                }, content = { Text("delete")}, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error))
            }
        }, title = {
            Text(text = "delete ${e::class.simpleName} ?")
        }, text = {
            Column {
                fields.forEach {
                    RenderField(it)
                }
            }
        }, modifier = Modifier.width(DialogSettings.defaultAlertDialogWidth))
    }
}

@Composable
private fun RenderField(field: EntityField, onFieldChange: ((EntityField) -> Unit)?=null) {
    if(onFieldChange==null){
        when (field) {
            is EntityField.BooleanField -> RenderBooleanFieldReadOnly(field)
            is EntityField.CaptionField -> RenderCaptionFieldReadOnly(field)
            is EntityField.EntityLink -> RenderEntityLinkReadOnly(field)
            is EntityField.EntityLinksList -> RenderEntityLinksListReadOnly(field)
            is EntityField.StringField -> RenderTextFieldReadOnly(field)
        }
    } else {
        when (field) {
            is EntityField.BooleanField -> RenderBooleanField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
            is EntityField.CaptionField -> RenderCaptionField(field)
            is EntityField.EntityLink -> RenderEntityLink(field, {}, {})
            is EntityField.EntityLinksList -> RenderEntityLinksList(field, {})
            is EntityField.StringField -> RenderTextField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
        }
    }
}
