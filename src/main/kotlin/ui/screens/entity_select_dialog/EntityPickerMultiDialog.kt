package ui.screens.entity_select_dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.unpack
import domain.application.GetListItemsUseCase
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.fieldsmappers.IFieldsMapper
import domain.entities.fieldsmappers.ItemFieldsMapper
import domain.entities.usecase_factories.IGetListUseCaseFactory
import kotlinx.coroutines.delay
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import persistence.datasources.ListItem
import persistence.repository.Specification
import test.Items
import ui.theme.DialogSettings
import kotlin.reflect.KClass

/**
 * Screen with compact list of entities with search field at the top.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : IEntity<*>> EntityPickerMultiDialog(
    entityClass: KClass<out T>,
    initialSelection: List<T> = listOf(),
    onDismiss: () -> Unit,
    onEntitiesSelected: (List<T>) -> Unit
) {
    val di = localDI()
    val fieldsMapperFactory by di.instance<FieldsMapperFactory>()
    val fieldsMapper = remember(fieldsMapperFactory, entityClass) { fieldsMapperFactory.getFieldsMapper(entityClass) }
    val getListUseCaseFactory by di.instance<IGetListUseCaseFactory>()

    val useCase = remember(entityClass, getListUseCaseFactory) { getListUseCaseFactory.getListUseCase(entityClass) }

    var searchString by remember { mutableStateOf("") }
    var entities by remember { mutableStateOf(listOf<T>()) }

    val state = rememberDialogState(
        size = DpSize(
            width = DialogSettings.defaultWideDialogWidth,
            height = DialogSettings.defaultWideDialogHeight
        )
    )

    Dialog(
        state = state,
        title = "select ${entityClass.simpleName}",
        onCloseRequest = onDismiss,
        content = {
            EntityMultiSelectDialogContent(
                entities = entities,
                fieldsMapper = fieldsMapper,
                initialSelection = initialSelection,
                searchString = searchString,
                onSearchStringChanged = {
                    searchString = it
                },
                onCancelClicked = onDismiss,
                onEntitiesSelected = {
                    onEntitiesSelected(it)
                    onDismiss()
                }
            )
        })

    LaunchedEffect(searchString) {
        if (searchString.isNotBlank())
            delay(1000L)

        val spec = when (searchString.isBlank()) {
            true -> Specification.QueryAll
            false -> Specification.Search(searchString)
        }
        entities = useCase(GetListItemsUseCase.Params.GetWithSpecification(specification = spec))
            .unpack()
            .mapNotNull { when(it){
                is ListItem.GroupedItem -> null
                is ListItem.NotGroupedItem -> it.item
            } }

    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun <T : IEntity<*>> EntityMultiSelectDialogContent(
    entities: List<T>,
    fieldsMapper: IFieldsMapper<T>,
    initialSelection: List<T> = listOf(),
    searchString: String = "",
    onSearchStringChanged: (String) -> Unit,
    onCancelClicked: () -> Unit,
    onEntitiesSelected: (List<T>) -> Unit
) {


    val checksMap = remember { initialSelection.map { it to true }.toMutableStateMap() }


    Surface {
        Column(modifier = Modifier.fillMaxHeight()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                stickyHeader {
                    Surface {
                        OutlinedTextField(
                            label = { Text(text = "search") },
                            modifier = Modifier.fillMaxWidth(),
                            value = searchString,
                            onValueChange = onSearchStringChanged,
                            trailingIcon = { Icon(Icons.Rounded.Search, "search field") }
                        )
                    }
                }


                items(entities) { entity ->
                    val entityIDs = remember(fieldsMapper, entity) { fieldsMapper.getEntityIDs() }
                    val fields =
                        remember(fieldsMapper, entityIDs) {
                            entityIDs.flatMap { fieldID ->
                                when (val field = fieldsMapper.getFieldByID(entity, fieldID)) {
                                    is EntityField.EntityLinksList -> field.entities
                                    else -> listOfNotNull(field)
                                }

                            }
                        }

                    val textString = remember(fields) {
                        val builder = StringBuilder()
                        fields.forEach {
                            builder.append(it.fieldID.name)
                            builder.append(": ")
                            builder.append(
                                when (it) {
                                    is EntityField.BooleanField -> it.value
                                    is EntityField.DateTimeField -> it.value ?: ""
                                    is EntityField.EntityLink -> it.entity?.toString() ?: ""
                                    is EntityField.EntityLinksList -> ""
                                    is EntityField.FloatField -> it.value
                                    is EntityField.LongField -> it.value
                                    is EntityField.StringField -> it.value
                                }
                            )
                            builder.append(" ")
                        }
                        builder.toString()
                    }
                    ListItem(
                        text = { Text(textString) },
                        icon = {
                            Checkbox(
                                checked = checksMap[entity] ?: false,
                                onCheckedChange = { checksMap[entity] = it })
                        })
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(modifier = Modifier.padding(4.dp), onClick = onCancelClicked) {
                    Text(text = "cancel")
                }

                Button(modifier = Modifier.padding(4.dp), onClick = {
                    onEntitiesSelected(checksMap.filterValues { it }.keys.toList()) //return only selected fixme initial order not preserved!
                }) {
                    Text(text = "ok")
                }
            }
        }
    }
}


@Preview
@Composable
fun ItemsMultiSelectDialogTest() {
    EntityMultiSelectDialogContent(
        entities = listOf(Items.Resistors.resistor1, Items.Capacitors.capacitor2),
        fieldsMapper = ItemFieldsMapper(),
        onSearchStringChanged = {},
        onCancelClicked = {},
        onEntitiesSelected = {}
    )
}