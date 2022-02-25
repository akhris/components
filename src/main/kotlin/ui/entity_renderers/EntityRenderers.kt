package ui.entity_renderers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.mouse.MouseScrollOrientation
import androidx.compose.ui.input.mouse.MouseScrollUnit
import androidx.compose.ui.input.mouse.mouseScrollFilter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.getName
import kotlinx.coroutines.delay
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
            Icon(
                modifier = Modifier.clickable { onValueChange(0f) },
                imageVector = Icons.Rounded.Clear,
                contentDescription = "clear text"
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
            Icon(
                modifier = Modifier.clickable { onValueChange("") },
                imageVector = Icons.Rounded.Clear,
                contentDescription = "clear text"
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
        Column {
            ListItem(
                modifier = Modifier.clickable {
                    onEntityLinkSelect()
                },
                text = { Text(text = field.getName()) },
                secondaryText = { Text(text = field.description) },
                trailing = {
                    Icon(
                        modifier = Modifier.clickable {
                            onEntityLinkClear()
                        },
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "clear entity"
                    )
                },
                icon = field.count?.let { c ->
                    {

                        var isHover by remember { mutableStateOf(false) }
                        var count by remember { mutableStateOf<Long?>(c) }

                        Box(modifier = Modifier
                            .height(56.dp)
                            .border(
                                width = 2.dp,
                                color = if (isHover) MaterialTheme.colors.primary else Color.Unspecified,
                                shape = MaterialTheme.shapes.small
                            )
                            .onPointerEvent(PointerEventType.Enter) { isHover = true }
                            .onPointerEvent(PointerEventType.Exit) { isHover = false }
                            .mouseScrollFilter { event, bounds ->

                                when (event.orientation) {
                                    MouseScrollOrientation.Vertical -> {
                                        when (val delta = event.delta) {
                                            is MouseScrollUnit.Line -> {
                                                count = count?.let {
                                                    it - sign(delta.value).toLong()
                                                }
                                                true
                                            }
                                            is MouseScrollUnit.Page -> false
                                        }
                                    }
                                    MouseScrollOrientation.Horizontal -> false
                                }

                            }) {
                            BasicTextField(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .width(IntrinsicSize.Min)
                                    .padding(8.dp),
                                value = count?.toString() ?: "",
                                onValueChange = {
                                    count = it.toLongOrNull()
                                }
                            )
                        }


                        //debounce logic:
                        LaunchedEffect(count) {
                            if (count == field.count) {
                                return@LaunchedEffect
                            }
                            //when count changes:
                            delay(500L)
                            count?.let {
                                onCountChanged?.invoke(it)
                            }
                        }

                    }
                }
            )


//            field.count?.let {

//                var count by remember { mutableStateOf<Long?>(it) }
//
//                OutlinedTextField(
//                    modifier = Modifier.align(Alignment.End),
//                    value = count?.toString() ?: "",
//                    onValueChange = { newValue ->
//                        count = newValue.toLongOrNull()
//                    },
//                    label = { Text(text = "quantity") },
//                    trailingIcon = {
//                        Column {
//                            Icon(modifier = Modifier.clickable {
//                                count = count?.let { it + 1 } ?: 1L
//                            }, imageVector = Icons.Rounded.Add, contentDescription = "count up")
//                            Icon(
//                                modifier = Modifier.clickable {
//                                    count = count?.let { it - 1 } ?: 0L
//                                },
//                                painter = painterResource("vector/remove_black_24dp.svg"),
//                                contentDescription = "count down"
//                            )
//                        }
//                    }
//                )

//                //debounce logic:
//                LaunchedEffect(count) {
//                    if (count == field.count) {
//                        return@LaunchedEffect
//                    }
//                    //when count changes:
//                    delay(500L)
//                    count?.let {
//                        onCountChanged?.invoke(it)
//                    }
//                }

//            }
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