package ui.screens.entity_renderers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import domain.entities.fieldsmappers.EntityField

@Composable
fun RenderTextFieldCell(
    modifier: Modifier = Modifier,
    field: EntityField.StringField,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        value = field.value,
//        label = { Text(text = field.fieldID.name) },
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
//        trailingIcon = {
//            Icon(
//                modifier = Modifier.clickable { onValueChange("") },
//                imageVector = Icons.Rounded.Clear,
//                contentDescription = "clear text"
//            )
//        }
    )
}