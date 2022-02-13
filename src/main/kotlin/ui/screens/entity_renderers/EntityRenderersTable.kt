package ui.screens.entity_renderers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import domain.entities.fieldsmappers.EntityField

@Composable
fun RowScope.RenderTextFieldCell(
    modifier: Modifier = Modifier,
    field: EntityField.StringField,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxHeight(),
        value = field.value,
//        label = { Text(text = field.fieldID.name) },
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun RowScope.RenderBooleanFieldCell(
    modifier: Modifier = Modifier,
    field: EntityField.BooleanField,
    onValueChange: (Boolean) -> Unit
) {
    Text(
        modifier = modifier.fillMaxWidth().align(Alignment.CenterVertically).clickable { onValueChange(!field.value) },
        text = when (field.value) {
            true -> "yes"
            false -> "no"
        },
        textAlign = TextAlign.Center
    )
}