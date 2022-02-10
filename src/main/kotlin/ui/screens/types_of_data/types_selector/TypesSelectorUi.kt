package ui.screens.types_of_data.types_selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import utils.toLocalizedString

@Composable
fun TypesSelectorUi(typesSelectorComponent: ITypesSelector) {

    val selectorState by typesSelectorComponent.models.subscribeAsState()

    val selectedType = remember(selectorState) { selectorState.selectedType }



    Column(modifier = Modifier.fillMaxHeight().selectableGroup()) {
        selectorState.types.forEachIndexed { index, typeOfObjects ->


            val background = when (selectedType == typeOfObjects) {
                true -> MaterialTheme.colors.primary
                false -> MaterialTheme.colors.surface
            }

            Text(
                modifier = Modifier.fillMaxWidth()
                    .selectable(
                        selected = selectedType == typeOfObjects,
                        onClick = { typesSelectorComponent.onTypeClicked(typeOfObjects) },
                        role = Role.RadioButton
                    )
                    .background(color = background)
                    .padding(8.dp),
                text = typeOfObjects.name?.toLocalizedString()?:"",
                color = MaterialTheme.colors.contentColorFor(background)
            )

            if (index != selectorState.types.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }
    }
}