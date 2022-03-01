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
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName
import kotlinx.coroutines.delay
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
    onEntityLinkClear: () -> Unit,
    onCountChanged: ((Long) -> Unit)? = null
) {
    if (field.entity != null) {
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
            ListItem(
                text = { Text(text = field.getName()) },
                secondaryText = { Text(text = field.description) },
                trailing = { ui.composable.Icons.ClearIcon(modifier = Modifier.clickable { onEntityLinkClear() }) }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderEntityLinksList(
    field: EntityField.EntityLinksList,
    onEntityLinkAdd: () -> Unit,
    onEntityLinkClear: (EntityField.EntityLink) -> Unit
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
            RenderEntityLink(link, {}, onEntityLinkClear = { onEntityLinkClear(link) })
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = onEntityLinkAdd,
                content = { Text("add ${field.fieldID.name}") })
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