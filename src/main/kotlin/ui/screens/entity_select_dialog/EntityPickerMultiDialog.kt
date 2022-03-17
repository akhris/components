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
import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.entities.IEntity
import com.akhris.domain.core.utils.unpack
import domain.entities.fieldsmappers.*
import domain.entities.usecase_factories.IGetListUseCaseFactory
import kotlinx.coroutines.delay
import org.kodein.di.compose.localDI
import org.kodein.di.instance
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
        entities = useCase(GetEntities.GetBySpecification(specification = spec)).unpack()

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
                val entityIDs = remember(fieldsMapper, entity) { fieldsMapper.getEntityIDs(entity).flatten() }
                val fields =
                    remember(fieldsMapper, entityIDs) { entityIDs.mapNotNull { fieldsMapper.getFieldByID(entity, it) } }

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
                onEntitiesSelected(checksMap.filterValues { it }.keys.toList()) //return only selected
            }) {
                Text(text = "ok")
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