@file:OptIn(ExperimentalFoundationApi::class)

package ui.entity_renderers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.utils.log
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName
import domain.valueobjects.Factor
import kotlinx.coroutines.delay
import strings.StringProvider
import ui.composable.FactorsDropDown
import ui.dialogs.DatePickerDialog
import ui.dialogs.TimePickerDialog
import utils.DateTimeConverter
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.math.sign


/**
 * Composable functions that render [EntityField.BooleanField]s
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderBooleanField(field: EntityField.BooleanField, stringProvider: StringProvider,onValueChange: (Boolean) -> Unit) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = field.fieldID.name
            )
        }, secondaryText = {
            Text(text = stringProvider.getLocalizedString(field.descriptionID))
        }, trailing = {
            Checkbox(field.value, onCheckedChange = onValueChange)
        })
}

@Composable
fun RenderFloatField(field: EntityField.FloatField, onValueChange: (Float) -> Unit) {

    var value by remember(field) { mutableStateOf(field.value.toString()) }

    val floatValue = remember(value) { value.toFloatOrNull() }

    TextField(
        isError = floatValue == null,
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = field.fieldID.name) },
        onValueChange = {
            value = it
            floatValue?.let { f ->
                onValueChange(f)
            }
        },
        trailingIcon = {
            ui.composable.Icons.ClearIcon(
                modifier = Modifier.clickable { onValueChange(0f) }
            )
        }
    )
}

@Composable
fun RenderTextField(field: EntityField.StringField, onValueChange: (String) -> Unit) {

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = field.value,
        label = if (field.isPlaceholder) null else {
            {
                Text(
                    text = field.fieldID.name
                )
            }
        },
        onValueChange = {
            if (field.isPlaceholder) {
                onValueChange(it.replace(field.value, ""))
            } else
                onValueChange(it)
        },
        trailingIcon = if (field.value.isNotEmpty() && !field.isPlaceholder) {
            {
                ui.composable.Icons.ClearIcon(
                    modifier = Modifier.clickable { onValueChange("") }
                )
            }
        } else null,
        textStyle = if (field.isPlaceholder) TextStyle(
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
            fontStyle = FontStyle.Italic
        ) else LocalTextStyle.current
    )

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RenderEntityLink(
    modifier: Modifier = Modifier,
    field: EntityField.EntityLink,
    stringProvider: StringProvider,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: ((EntityField.EntityLink) -> Unit)? = null,
    onEntityLinkChanged: ((EntityField.EntityLink) -> Unit)? = null
) {
    if (field.entity != null) {

        when (field) {
            is EntityField.EntityLink.EntityLinkCountable -> RenderCountableEntityLink(
                field = field,
                stringProvider = stringProvider,
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) },
                onCountChanged = {
                    onEntityLinkChanged?.invoke(field.copy(count = it))
                }
            )
            is EntityField.EntityLink.EntityLinkSimple -> RenderCommonEntityLink(
                name = field.getName(),
                description = stringProvider.getLocalizedString(field.descriptionID),
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) }
            )
            is EntityField.EntityLink.EntityLinkValuable -> RenderValuableEntityLink(
                field = field,
                stringProvider = stringProvider,
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) },
                onFieldChanged = onEntityLinkChanged

            )
        }

    } else {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = { onEntityLinkSelect() },
                content = { Text("add ${field.fieldID.name}") })
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun RenderCountableEntityLink(
    field: EntityField.EntityLink.EntityLinkCountable,
    stringProvider: StringProvider,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: () -> Unit,
    onCountChanged: ((Long) -> Unit)? = null
) {

    Row(
        modifier = Modifier.wrapContentHeight().clickable { onEntityLinkSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        field.count?.let { c ->
            var isHover by remember { mutableStateOf(false) }
            var count by remember { mutableStateOf<Long?>(c) }
            var multiplier by remember { mutableStateOf(1) }
            Box(modifier = Modifier
                .height(40.dp)
                .widthIn(min = 40.dp)
                .padding(start = 16.dp)
                .border(
                    width = if (multiplier == 1) 2.dp else 4.dp,
                    color = if (isHover) MaterialTheme.colors.primary else Color.Unspecified,
                    shape = MaterialTheme.shapes.small
                )
                .onFocusEvent {
                    multiplier = if (it.isFocused) 5 else 1
                }
                .onPointerEvent(PointerEventType.Enter) { isHover = true }
                .onPointerEvent(PointerEventType.Exit) { isHover = false }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Scroll) {
                                count = java.lang.Long.max(count?.let {
                                    val delta = event.changes.first().scrollDelta
                                    it - sign(delta.y).toLong() * multiplier
                                } ?: 0L, 1L)
                                event.changes.first().consume()
                            }
                        }
                    }
                }
            ) {
                BasicTextField(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(IntrinsicSize.Min)
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    value = count?.toString() ?: "",
                    onValueChange = { count = it.toLongOrNull() }
                )
            }

            //debounce logic:
            LaunchedEffect(count) {
                if (count == field.count) {
                    return@LaunchedEffect
                }
                //when count changes:
                delay(500L)
                count?.let { onCountChanged?.invoke(it) }
            }
        }
        RenderCommonEntityLink(field.getName(), stringProvider.getLocalizedString(field.descriptionID), onEntityLinkSelect, onEntityLinkClear)
    }
}

@Composable
private fun RenderValuableEntityLink(
    field: EntityField.EntityLink.EntityLinkValuable,
    stringProvider: StringProvider,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: () -> Unit,
    onFieldChanged: ((EntityField.EntityLink.EntityLinkValuable) -> Unit)? = null
) {
    var value by remember(field) { mutableStateOf(field.value) }
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { onEntityLinkSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TooltipArea(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            tooltip = {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Text(modifier = Modifier.padding(4.dp), text = stringProvider.getLocalizedString(field.descriptionID))
                }
            }, content = {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value ?: "",
                    onValueChange = { value = it },
                    label = {
                        val labelBuilder = StringBuilder(field.getName())
                        field.unit?.let {
                            labelBuilder.append(", $it")
                        }
                        Text(labelBuilder.toString())
                    }
                )
            })
        if (field.factor != null) {
            FactorsDropDown(modifier = Modifier.width(56.dp).height(40.dp), onFactorSelected = { f ->
                log("onFactorSelected: $f")
                onFieldChanged?.invoke(field.copy(factor = f.factor))
            }, factor = Factor.parse(field.factor))
        }

    }


    //debounce logic:
    LaunchedEffect(value) {
        if (value == field.value) {
            return@LaunchedEffect
        }
        //when count changes:
        delay(500L)
        value?.let { onFieldChanged?.invoke(field.copy(value = it)) }
    }
//    RenderCommonEntityLink(field.getName(), field.description, onEntityLinkSelect, onEntityLinkClear)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderCommonEntityLink(
    name: String,
    description: String,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: () -> Unit
) {
    ListItem(modifier = Modifier.clickable { onEntityLinkSelect() },
        text = { Text(text = name) },
        secondaryText = { Text(text = description) },
        trailing = { ui.composable.Icons.ClearIcon(modifier = Modifier.clickable { onEntityLinkClear() }) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksList(
    field: EntityField.EntityLinksList,
    stringProvider: StringProvider,
    onEntityLinkAdd: () -> Unit,
    onEntityLinkClear: (EntityField.EntityLink) -> Unit,
    onEntityLinkChanged: (EntityField.EntityLink) -> Unit,
    onListChanged: ((List<EntityField.EntityLink>) -> Unit)? = null
) {

    Column {

        ListItem(
            text = {
                Text(
                    text = "${field.fieldID.name}:",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
            },
            secondaryText = {
                Text(
                    text = stringProvider.getLocalizedString(field.descriptionID),
                    textAlign = TextAlign.Center
                )
            }
        )


        field.entities.forEachIndexed { index, link ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.padding(8.dp)) {
                    if (index > 0)
                        Icon(
                            modifier = Modifier.clickable {
                                val mutableList = field.entities.toMutableList()
                                Collections.swap(mutableList, index, index - 1)
                                onListChanged?.invoke(mutableList)
                            },
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = "drag to top",
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                    if (index < field.entities.size - 1)
                        Icon(
                            modifier = Modifier.clickable {
                                val mutableList = field.entities.toMutableList()
                                Collections.swap(mutableList, index, index + 1)
                                onListChanged?.invoke(mutableList)
                            },
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "drag to bottom",
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
                        )
                }
                RenderEntityLink(
                    modifier = Modifier.weight(1f),
                    field = link,
                    stringProvider = stringProvider,
                    onEntityLinkChanged = onEntityLinkChanged,
                    onEntityLinkClear = { onEntityLinkClear(link) },
                    onEntityLinkSelect = {})
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onEntityLinkAdd,
                content = { Text("edit ${field.fieldID.name}") })
        }

    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderDateTime(field: EntityField.DateTimeField, onDateChanged: (LocalDateTime?) -> Unit) {

    var datePickerDialogShow by remember { mutableStateOf(false) }
    var timePickerDialogShow by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.clickable { datePickerDialogShow = true },
        text = { field.value?.let { dateTime -> Text(DateTimeConverter.dateTimeToString(dateTime)) } },
        secondaryText = { Text(text = field.descriptionID) }
    )

    if (datePickerDialogShow) {
        DatePickerDialog(
            initialSelection = field.value?.toLocalDate(),
            onDismiss = { datePickerDialogShow = false },
            onDateSelected = { newDate ->
                onDateChanged(field.value?.with(TemporalAdjusters.ofDateAdjuster {
                    newDate
                }))
                timePickerDialogShow = true
            }
        )
    }

    if (timePickerDialogShow) {
        TimePickerDialog(initialTime = field.value, onDismiss = { timePickerDialogShow = false }, onTimeSelected = {
            onDateChanged(it)
        })
    }

}