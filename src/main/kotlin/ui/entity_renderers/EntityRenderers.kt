package ui.entity_renderers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.utils.log
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName
import domain.valueobjects.Factor
import kotlinx.coroutines.delay
import ui.composable.FactorsDropDown
import ui.dialogs.DatePickerDialog
import ui.dialogs.TimePickerDialog
import utils.DateTimeConverter
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import kotlin.math.sign


/**
 * Composable functions that render [EntityField.BooleanField]s
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderBooleanField(field: EntityField.BooleanField, onValueChange: (Boolean) -> Unit) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = field.fieldID.name
            )
        }, secondaryText = {
            Text(text = field.description)
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
        label = { Text(text = field.fieldID.name) },
        onValueChange = onValueChange,
        trailingIcon = {
            ui.composable.Icons.ClearIcon(
                modifier = Modifier.clickable { onValueChange("") }
            )
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RenderEntityLink(
    field: EntityField.EntityLink,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: ((EntityField.EntityLink) -> Unit)? = null,
    onEntityLinkChanged: ((EntityField.EntityLink) -> Unit)? = null
) {
    if (field.entity != null) {

        when (field) {
            is EntityField.EntityLink.EntityLinkCountable -> RenderCountableEntityLink(
                field = field,
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) },
                onCountChanged = {
                    onEntityLinkChanged?.invoke(field.copy(count = it))
                }
            )
            is EntityField.EntityLink.EntityLinkSimple -> RenderCommonEntityLink(
                name = field.getName(),
                description = field.description,
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) }
            )
            is EntityField.EntityLink.EntityLinkValuable -> RenderValuableEntityLink(
                field = field,
                onEntityLinkSelect = onEntityLinkSelect,
                onEntityLinkClear = { onEntityLinkClear?.invoke(field) },
                onFieldChanged = {
                    onEntityLinkChanged?.invoke(it)
                }
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
        RenderCommonEntityLink(field.getName(), field.description, onEntityLinkSelect, onEntityLinkClear)
    }
}

@Composable
private fun RenderValuableEntityLink(
    field: EntityField.EntityLink.EntityLinkValuable,
    onEntityLinkSelect: () -> Unit,
    onEntityLinkClear: () -> Unit,
    onFieldChanged: ((EntityField.EntityLink.EntityLinkValuable) -> Unit)? = null
) {
    var value by remember(field) { mutableStateOf(field.value) }
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable { onEntityLinkSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            value = value ?: "",
            onValueChange = { value = it },
            label = {
                val labelBuilder = StringBuilder(field.getName())
                field.unit?.let{
                    labelBuilder.append(", $it")
                }
                Text(labelBuilder.toString())
            }
        )
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
    ListItem(
        text = { Text(text = name) },
        secondaryText = { Text(text = description) },
        trailing = { ui.composable.Icons.ClearIcon(modifier = Modifier.clickable { onEntityLinkClear() }) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksList(
    field: EntityField.EntityLinksList,
    onEntityLinkAdd: () -> Unit,
    onEntityLinkClear: (EntityField.EntityLink) -> Unit,
    onEntityLinkChanged: (EntityField.EntityLink) -> Unit
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
            secondaryText = { Text(text = field.description, textAlign = TextAlign.Center) }
        )

        field.entities.forEach { link ->
            RenderEntityLink(
                link,
                onEntityLinkChanged = onEntityLinkChanged,
                onEntityLinkClear = { onEntityLinkClear(link) },
                onEntityLinkSelect = {})
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
        secondaryText = { Text(text = field.description) }
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