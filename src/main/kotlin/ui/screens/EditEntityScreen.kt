package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import com.akhris.domain.core.entities.IEntity
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.IFieldsMapper
import domain.entities.fieldsmappers.IFieldsMapperFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.entity_renderers.*
import ui.theme.DialogSettings


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity<*>> EditEntityDialog(
    title: String,
    entity: T,
    onSaveClicked: (T) -> Unit,
    onDismiss: () -> Unit
) {

    val dialogState = rememberDialogState(size = DpSize.Unspecified)

    Dialog(
        state = dialogState,
        onCloseRequest = onDismiss,
        content = {
            EditEntityScreenContent(entity, onSaveClicked = { changedEntity ->
                onSaveClicked(changedEntity)
                onDismiss()
            }, onCancelClicked = onDismiss)
        },
        title = title,
        resizable = false,
        icon = painterResource("vector/edit_black_24dp.svg")
    )

}

@Composable
fun <T : IEntity<*>> EditEntityScreenContent(initEntity: T, onSaveClicked: (T) -> Unit, onCancelClicked: () -> Unit) {
    val di = localDI()
    val factory: IFieldsMapperFactory by di.instance()
    val mapper = remember(factory) { factory.getFieldsMapper(initEntity::class) }

    var entity by remember(initEntity) { mutableStateOf(initEntity) }
    val fields = remember(mapper, entity) { mapper.mapFields(entity) }




    Column(modifier = Modifier.padding(8.dp).width(DialogSettings.defaultWideDialogWidth)) {
        fields.forEach {
            RenderField(it, mapper, entity, onEntityChanged = { newEntity ->
                entity = newEntity
            }, onEntityLinkSelect = {

            })
        }
        //buttons
//        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(modifier = Modifier.padding(ButtonDefaults.ContentPadding), onClick = onCancelClicked,
                content = {
                    Text("cancel")
                }
            )

            Button(modifier = Modifier.padding(ButtonDefaults.ContentPadding), onClick = {
                onSaveClicked(entity)
            }, content = {
                Text("save")
            })
        }
    }

}

@Composable
private fun <T : IEntity<*>> RenderField(
    field: EntityField,
    mapper: IFieldsMapper,
    entity: T,
    onEntityChanged: (T) -> Unit,
    onEntityLinkSelect: () -> Unit
) {
    when (field) {
        is EntityField.StringField -> RenderTextField(
            field = field,
            onValueChange = { newValue -> onEntityChanged(mapper.mapIntoEntity(entity, field.copy(value = newValue))) })
        is EntityField.BooleanField -> RenderBooleanField(
            field = field,
            onValueChange = { newValue ->
                onEntityChanged(mapper.mapIntoEntity(entity, field.copy(value = newValue)))
            }
        )
        is EntityField.EntityLink -> RenderEntityLink(
            field = field,
            onEntityLinkSelect = onEntityLinkSelect,
            onEntityLinkClear = {
                onEntityChanged(mapper.mapIntoEntity(entity, field.copy(entity = null)))
            }
        )
        is EntityField.EntityLinksList -> RenderEntityLinksList(field = field, onEntityLinkAdd = {})
        is EntityField.CaptionField -> RenderCaptionField(field = field)
    }
}


