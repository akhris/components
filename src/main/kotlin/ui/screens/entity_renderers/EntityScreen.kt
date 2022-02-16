package ui.screens.entity_renderers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.log
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.FieldsMapperFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.types_of_data.types_selector.ItemRepresentationType
import ui.theme.ContentSettings
import ui.theme.DialogSettings
import kotlin.reflect.KClass

@Composable
fun EntityScreen() {

}


@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> EntityScreenContent(
    itemRepresentationType: ItemRepresentationType = ItemRepresentationType.Card,
    entities: List<T>,
    onEntityRemoved: ((T) -> Unit)? = null,
    onEntityUpdated: ((T) -> Unit)? = null
) {

    val lazyColumnState = rememberLazyListState()

//    val headerElevation by animateDpAsState(
//        if (lazyColumnState.firstVisibleItemIndex == 0) {
//            ContentSettings.stickyHeaderElevationOnRest
//        } else ContentSettings.stickyHeaderElevationOnScroll
//    )

    val di = localDI()
    val factory: FieldsMapperFactory by di.instance()

    val mapper = remember(entities, factory) { entities.firstOrNull()?.let { factory.getFieldsMapper(it::class) } }


    //main title
//        entityType?.let {
//            stickyHeader(key = "main title") {
//                Surface(modifier = Modifier.fillMaxWidth(), elevation = headerElevation) {
//                    ListItem(
//                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 2.dp),
//                        text = {
//                            Text(
//                                text = entityType.name?.toLocalizedString() ?: "",
//                                style = MaterialTheme.typography.h3
//                            )
//                        },
//                        secondaryText = {
//                            Text(text = entityType.description?.toLocalizedString() ?: "")
//                        }
//                    )
//                }
//            }
//        }

    //content depending on representation type:
    when (itemRepresentationType) {
        //cards
        ItemRepresentationType.Card -> {
            LazyColumn(state = lazyColumnState) {
                items(items = entities, key = { entity -> entity.id ?: "no_id" }, itemContent = { entity ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        RenderCardEntity(
                            entity,
                            onEntityChanged = {
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
//                stickyHeader(key = "table_header") {
//                    LazyRow {
//                        itemsIndexed(fieldsSet.toList()) { index, field ->
//                            Text(
//                                modifier = Modifier.border(width = 2.dp, color = Color.DarkGray).padding(4.dp),
//                                text = field.name
//                            )
//                        }
//                    }
//                }
//
//                itemsIndexed(entities) { index, entity ->
//                    val fields = fieldsSet.mapNotNull { mapper?.getFieldByID(entity, fieldsSet.elementAt(index)) }
//                    LazyRow {
//                        items(fields) {
//
//                            Text(
//                                modifier = Modifier.border(width = 2.dp, color = Color.DarkGray).padding(4.dp),
//                                text = when (it) {
//                                    is EntityField.BooleanField -> it.value.toString()
//                                    is EntityField.CaptionField -> it.caption
//                                    is EntityField.EntityLink -> ""
//                                    is EntityField.EntityLinksList -> ""
//                                    is EntityField.FavoriteField -> it.isFavorite.toString()
//                                    is EntityField.FloatField -> it.value.toString()
//                                    is EntityField.StringField -> it.value
//                                    is EntityField.URLField -> it.url
//                                }
//                            )
//                        }
//                    }
//                }


        }

//        entities.forEach { entity ->
//            RenderEntity(
//                entity,
//                onEntityChanged = { println("entity changed: $it") },
//                onEntityRemoved = onEntityRemoved
//            )
//        }
    }


}

@Composable
fun <T : IEntity<*>> LazyListScope.renderCards(entities: List<T>, onEntityRemoved: ((T) -> Unit)? = null) {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> LazyListScope.renderTable(entities: List<T>, onEntityRemoved: ((T) -> Unit)? = null) {


//
//    items(items = entities, key = { entity -> entity.id ?: "no_id" }, itemContent = { entity ->
//
//
//        Box(modifier = Modifier.fillMaxWidth()) {
//            RenderCardEntity(
//                entity,
//                onEntityChanged = { println("entity changed: $entity") },
//                onEntityRemoved = onEntityRemoved
//            )
//        }
//    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity<*>> BoxScope.RenderCardEntity(
    initialEntity: T,
    onEntityChanged: (T) -> Unit,
    onEntityRemoved: ((T) -> Unit)? = null,
    onAddEntityClicked: (() -> Unit)? = null
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
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            fields.forEach {
                RenderField(
                    it,
                    onFieldChange = { changedField ->
                        entity = mapper.mapIntoEntity(entity, changedField)
                        log("entity after mapping: $entity")
//                    onEntityChanged(mapper.mapIntoEntity(initialEntity, changedField))
                    }
                )
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

@Composable
private fun RenderField(
    field: EntityField,
    onFieldChange: ((EntityField) -> Unit)? = null
) {
    if (onFieldChange == null) {
        //render read-only mode
        when (field) {
            is EntityField.BooleanField -> RenderBooleanFieldReadOnly(field)
            is EntityField.EntityLink<*> -> RenderEntityLinkReadOnly(field)
            is EntityField.EntityLinksList<*> -> RenderEntityLinksListReadOnly(field)
            is EntityField.StringField -> RenderTextFieldReadOnly(field)
            is EntityField.FloatField -> RenderFloatFieldReadOnly(field)
            is EntityField.DateTimeField -> {}
            is EntityField.LongField -> {}
        }
    } else {
        when (field) {
            is EntityField.BooleanField -> RenderBooleanField(
                field,
                onValueChange = { newValue -> onFieldChange(field.copy(value = newValue)) })
            is EntityField.EntityLink<*> -> RenderEntityLink(field, {}, onEntityLinkClear = {
                onFieldChange(field.copy(entity = null))
            })
            is EntityField.EntityLinksList<*> -> RenderEntityLinksList(
                field,
                onEntityLinkAdd = {
                    //on add entity clicked
                    println("going to add entity of type:")
                    println(field.entities.mapNotNull { it.entity }.getEntityType())
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
            is EntityField.DateTimeField -> {}
            is EntityField.LongField -> {}
        }
    }
}

inline fun <reified T : IEntity<*>> List<T>.getEntityType(): KClass<T> = T::class
