package ui.entity_renderers

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import domain.entities.EntityValuable
import domain.entities.Item
import domain.entities.ObjectType
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
    onEntityUpdated: ((T) -> Unit)? = null,
    onEntityCopied: ((T) -> Unit)? = null
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
                            onEntityRemoved = onEntityRemoved,
                            onEntityCopyClicked = onEntityCopied
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
 * todo: add collapsible/expandable feature
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> BoxScope.RenderCardEntity(
    initialEntity: T,
    onEntitySaveClicked: (T) -> Unit,
    onEntityCopyClicked: ((T) -> Unit)? = null,
    onEntityRemoved: ((T) -> Unit)? = null
) {
    val di = localDI()
    val factory: FieldsMapperFactory by di.instance()
    val mapper = remember(factory) { factory.getFieldsMapper(initialEntity::class) }
    var showDeletePrompt by remember { mutableStateOf<T?>(null) }
    var objectTypeField by remember { mutableStateOf<EntityField.EntityLink?>(null) }
    var entity by remember(initialEntity) { mutableStateOf(initialEntity) }
    var isExpanded by remember { mutableStateOf(false) }

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


        Column {

            fields
                .take(if (isExpanded) fields.size else 1)
                .forEach {
                    RenderField(
                        it,
                        onFieldChange = { changedField ->
                            log("onFieldChange: $changedField")
                            if (initialEntity::class == Item::class) {
                                val entityField = changedField as? EntityField.EntityLink
                                val objectType = entityField?.entity as? ObjectType
                                if (objectType != null) {
                                    //check if this is object type and item, and add new parameters if necessary
                                    objectTypeField = entityField
                                } else {
                                    //
                                    entity = mapper.mapIntoEntity(entity, changedField)
                                }
                            } else {
                                entity = mapper.mapIntoEntity(entity, changedField)
                            }
                        })

                }
            //buttons row:
            if (isExpanded)
                Divider(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //expand/collapse:
                val rotation by animateFloatAsState(if (isExpanded) 0f else 180f)
                TooltipArea(tooltip = {
                    Surface(
                        modifier = Modifier.shadow(4.dp),
                        color = Color(255, 255, 210),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "collapse card" else "expand card",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowUp,
                                contentDescription = if (isExpanded) "collapse" else "expand",
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    )
                }


                Spacer(modifier = Modifier.weight(1f))

                if (entity != initialEntity) {
                    TextButton(
                        onClick = { entity = initialEntity },
                        content = { Text(text = "discard") })
                    Button(
                        onClick = { onEntitySaveClicked(entity) },
                        content = { Text(text = "save") })
                } else {
                    //copy button
                    onEntityCopyClicked?.let {
                        TextButton(
                            onClick = { it(entity) },
                            content = { Text(text = "copy") })
                    }

                    //delete button
                    onEntityRemoved?.let { onRemoveCallback ->
                        TextButton(
                            onClick = { showDeletePrompt = initialEntity },
                            content = { Text(text = "delete", color = MaterialTheme.colors.error) })
                    }
                }
            }
        }


    }



    objectTypeField?.let { objectField ->
        val item = entity as? Item
        val objectType = objectField.entity as? ObjectType
        if (item != null && objectType != null)
            CheckItemObjectType(item, objectType, onItemChanged = {
                entity = mapper.mapIntoEntity(it as T, objectField)

            }, onDismiss = {
                objectTypeField = null
            })

//        entity = mapper.mapIntoEntity(entity, fieldToCheck)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CheckItemObjectType(
    item: Item,
    objectType: ObjectType,
    withAlert: Boolean = true,
    onItemChanged: (Item) -> Unit,
    onDismiss: () -> Unit
) {

    //entity is item and changed field is ObjectType.
    var showAlert by remember { mutableStateOf(false) }

    if (item.type?.id != objectType.id) {
        showAlert = true
    }
    //new object type is different from entity's object type
    //ask for adding new parameters to entity:

    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                showAlert = false
                onDismiss()
            },
            text = { Text("Add parameters for ${objectType.name}?") },
            confirmButton = {
                Button(content = { Text("Yes") }, onClick = {
                    //add parameters to entity
                    onItemChanged(
                        item.copy(values = objectType.parameters.map { p -> EntityValuable(p) })
                    )
                    onDismiss()
                })
            })
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
            is EntityField.EntityLink -> RenderEntityLink(
                field,
                onEntityLinkSelect = {
                    //on entity select clicked
                    println("going to select entity of type:")
                    println(field.fieldID.entityClass)
                    showSelectEntityDialog = field.fieldID.entityClass
                }, onEntityLinkChanged = onFieldChange,
                onEntityLinkClear = {
                    onFieldChange(
                        when (field) {
                            is EntityField.EntityLink.EntityLinkCountable -> field.copy(entity = null)
                            is EntityField.EntityLink.EntityLinkSimple -> field.copy(entity = null)
                            is EntityField.EntityLink.EntityLinkValuable -> field.copy(entity = null)
                        }
                    )
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
                },
                onEntityLinkChanged = onFieldChange
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
                val changedField =
                    (field as? EntityField.EntityLink)?.let {
                        when (it) {
                            is EntityField.EntityLink.EntityLinkCountable -> it.copy(entity = changedEntity)
                            is EntityField.EntityLink.EntityLinkSimple -> it.copy(entity = changedEntity)
                            is EntityField.EntityLink.EntityLinkValuable -> it.copy(entity = changedEntity)
                        }
                    }
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
                    val newEntitiesList = changedEntitiesList
                        .mapIndexed { index, iEntity ->
                            val entityFieldID = EntityFieldID.EntityID(
                                tag = "${ell.fieldID.tag}$index",
                                name = "${iEntity::class.simpleName} ${index + 1}",
                                entityClass = entityClass
                            )
                            val fromOldList = ell.entities.find { it.entity?.id == iEntity.id }
                            fromOldList?.let {
                                when (it) {
                                    is EntityField.EntityLink.EntityLinkCountable -> it.copy(fieldID = entityFieldID)
                                    is EntityField.EntityLink.EntityLinkSimple -> it.copy(fieldID = entityFieldID)
                                    is EntityField.EntityLink.EntityLinkValuable -> it.copy(fieldID = entityFieldID)
                                }
                            } ?: EntityField.EntityLink.EntityLinkSimple(
                                fieldID = entityFieldID,
                                entity = iEntity
//                                entityClass = entityClass
                            )
                        }

                    onFieldChange?.invoke(
                        ell.copy(
                            entities = newEntitiesList
                        )
                    )
                }
            },
            initialSelection = fieldToChange?.entities?.mapNotNull { it.entity } ?: listOf()
        )
    }
}
