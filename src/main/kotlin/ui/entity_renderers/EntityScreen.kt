package ui.entity_renderers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.EntityFieldID
import domain.entities.fieldsmappers.FieldsMapperFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType
import ui.screens.entity_select_dialog.EntityPickerMultiDialog
import ui.screens.entity_select_dialog.EntityPickerSingleDialog
import ui.theme.ContentSettings
import ui.theme.DialogSettings
import kotlin.reflect.KClass

/**
 * Entity screen content - renders list of Entities in a way that depends on [itemRepresentationType] parameter.
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> EntityScreenContent(
    itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Card,
    entities: List<T>,
    onEntityRemoved: ((T) -> Unit)? = null,
    onEntityUpdated: ((T) -> Unit)? = null
) {

    val lazyColumnState = rememberLazyListState()

    val di = localDI()
    val factory: FieldsMapperFactory by di.instance()

    val mapper = remember(entities, factory) { entities.firstOrNull()?.let { factory.getFieldsMapper(it::class) } }

    //content depending on representation type:
    when (itemRepresentationType) {
        //cards
        ItemRepresentationType.Card -> {
            LazyColumn(state = lazyColumnState) {
                items(items = entities, key = { entity -> entity.id ?: "no_id" }, itemContent = { entity ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        RenderCardEntity(
                            entity,
                            onEntitySaveClicked = {
                                log("entity changed: $it")
                                onEntityUpdated?.invoke(it)
                            },
                            onEntityRemoved = onEntityRemoved
                        )
                    }
                })
            }
        }

        //table
        ItemRepresentationType.Table -> {
            mapper?.let {
                EntityTableContent(entities = entities, fieldsMapper = it)
            }
        }
    }


}

/**
 * Renders entity in a card way - all it's fields in a column one by one.
 * At the bottom of the card there are three buttons: delete entity, save changes and discard changes.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity<*>> BoxScope.RenderCardEntity(
    initialEntity: T,
    onEntitySaveClicked: (T) -> Unit,
    onEntityRemoved: ((T) -> Unit)? = null
//    onAddEntityClicked: (() -> Unit)? = null
) {
    val di = localDI()
    val factory: FieldsMapperFactory by di.instance()
    val mapper = remember(factory) { factory.getFieldsMapper(initialEntity::class) }
    var showDeletePrompt by remember { mutableStateOf<T?>(null) }

    var entity by remember(initialEntity) { mutableStateOf(initialEntity) }

    val fields =
        remember(mapper, entity) {
            mapper.getEntityIDs(entity = entity).mapNotNull { mapper.getFieldByID(entity, it) }
        }


    Card(
        modifier = Modifier
            .align(Alignment.Center)
            .widthIn(min = ContentSettings.contentCardMinWidth, max = ContentSettings.contentCardMaxWidth)
            .padding(8.dp),
        elevation = 0.dp,
        border = BorderStroke(Dp.Hairline, color = Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            fields.forEach {
                RenderField(
                    it,
                    onFieldChange = { changedField ->
                        log("changedField: $changedField")
                        if (changedField is EntityField.EntityLinksList) {
                            log("changedField is EntityLinksList")
                            changedField.entities.forEach {
                                log("link: $it")
                            }
                        }
                        entity = mapper.mapIntoEntity(entity, changedField)
                        log("entity after mapping: $entity")
//                    onEntityChanged(mapper.mapIntoEntity(initialEntity, changedField))
                    }
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                onEntityRemoved?.let { onRemoveCallback ->
                    TextButton(
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                        onClick = { showDeletePrompt = initialEntity },
                        content = { Text(text = "delete", color = MaterialTheme.colors.error) })
                }


                if (entity != initialEntity) {
                    TextButton(
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                        onClick = { entity = initialEntity },
                        content = { Text(text = "discard") })
                    Button(
                        modifier = Modifier.padding(ButtonDefaults.ContentPadding),
                        onClick = { onEntitySaveClicked(entity) },
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

                Button(
                    onClick = {
                        onEntityRemoved?.invoke(e)
                        showDeletePrompt = null
                    },
                    content = { Text("delete") },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                )
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

/**
 * Renders [EntityField] depending on it's type. And depending on [onFieldChange] callback.
 * If callback is null - render in read-only mode.
 */
@Composable
private fun RenderField(
    field: EntityField,
    onFieldChange: ((EntityField) -> Unit)? = null
) {
    var showSelectEntityDialog by remember { mutableStateOf<KClass<out IEntity<*>>?>(null) }
    var showSelectEntitiesDialog by remember { mutableStateOf<KClass<out IEntity<*>>?>(null) }


    if (onFieldChange == null || field.fieldID.isReadOnly) {
        //render read-only mode
        when (field) {
            is EntityField.BooleanField -> RenderBooleanFieldReadOnly(field)
            is EntityField.EntityLink -> RenderEntityLinkReadOnly(field)
            is EntityField.EntityLinksList -> RenderEntityLinksListReadOnly(field)
            is EntityField.StringField -> RenderTextFieldReadOnly(field)
            is EntityField.FloatField -> RenderFloatFieldReadOnly(field)
            is EntityField.DateTimeField -> RenderDateTimeReadOnly(field)
            is EntityField.LongField -> {}
        }
    } else {
        when (field) {
            is EntityField.BooleanField -> RenderBooleanField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
            is EntityField.EntityLink -> RenderEntityLink(field, onEntityLinkSelect = {
                //on entity select clicked
                println("going to select entity of type:")
                println(field.entityClass)
                showSelectEntityDialog = field.entityClass
            }, onEntityLinkClear = {
                onFieldChange(field.copy(entity = null))
            }, onCountChanged = {
                onFieldChange(field.copy(count = it))
            }
            )
            is EntityField.EntityLinksList -> RenderEntityLinksList(
                field,
                onEntityLinkAdd = {
                    //on add entity clicked
                    println("going to add entity of type:")
                    println(field.entityClass)
                    showSelectEntitiesDialog = field.entityClass
                },
                onEntityLinkClear = {
                    onFieldChange(field.copy(entities = field.entities.minus(it)))
                }
            )
            is EntityField.StringField -> RenderTextField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
            is EntityField.FloatField -> RenderFloatField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
            is EntityField.DateTimeField -> RenderDateTime(field, onDateChanged = { newDate ->
                onFieldChange(field.copy(value = newDate))
            })
            is EntityField.LongField -> {}
        }
    }

    showSelectEntityDialog?.let { entityClass ->
        EntityPickerSingleDialog(
            entityClass,
            onDismiss = { showSelectEntityDialog = null },
            onEntitySelected = { changedEntity ->
                val changedField = (field as? EntityField.EntityLink)?.copy(entity = changedEntity)
                changedField?.let { cf ->
                    onFieldChange?.invoke(cf)
                }
            }
        )
    }

    showSelectEntitiesDialog?.let { entityClass ->

        val fieldToChange = field as? EntityField.EntityLinksList

        EntityPickerMultiDialog(
            entityClass,
            onDismiss = { showSelectEntitiesDialog = null },
            onEntitiesSelected = { changedEntitiesList ->
                fieldToChange?.let { ell ->
                    onFieldChange?.invoke(
                        ell.copy(
                            entities = changedEntitiesList.mapIndexed { index, iEntity ->
                                EntityField.EntityLink(
                                    fieldID = EntityFieldID.EntityID(
                                        tag = "${ell.fieldID.tag}$index",
                                        name = "${iEntity::class.simpleName} ${index + 1}",
                                        entityClass = entityClass
                                    ),
                                    entity = iEntity,
                                    entityClass = entityClass
                                )
                            }
                        )
                    )
                }
            },
            initialSelection = fieldToChange?.entities?.mapNotNull { it.entity } ?: listOf()
        )
    }
}
