package ui.screens.types_of_data.types_selector

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import utils.toLocalizedString

@Composable
fun TypesSelectorUi(typesSelectorComponent: ITypesSelector) {

    val selectorState by typesSelectorComponent.models.subscribeAsState()

    val selectedType = remember(selectorState) { selectorState.selectedType }


    val representationTypes = remember {
        listOf(
            ItemRepresentationType.Card,
            ItemRepresentationType.Table
        )
    }

    Column(modifier = Modifier.fillMaxHeight().selectableGroup()) {
        //representation type selector
        RepresentationTypesSelector(
            representationTypes = representationTypes,
            currentType = selectorState.itemRepresentationType,
            onTypeChanged = {
                typesSelectorComponent.onItemRepresentationTypeChanged(it)
            })

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
                text = typeOfObjects.name?.toLocalizedString() ?: "",
                color = MaterialTheme.colors.contentColorFor(background)
            )

            if (index != selectorState.types.lastIndex) {
                Divider(modifier = Modifier.fillMaxWidth().height(1.dp))
            }
        }
    }
}


@Composable
fun RepresentationTypesSelector(
    representationTypes: List<ItemRepresentationType>,
    currentType: ItemRepresentationType,
    onTypeChanged: (ItemRepresentationType) -> Unit
) {
    val cornerRadius = remember { 8.dp }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {


        representationTypes.forEachIndexed { index, itemRepresentationType ->

            OutlinedButton(
                onClick = { onTypeChanged(itemRepresentationType) },
                shape = when (index) {
                    // left outer button
                    0 -> RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = cornerRadius,
                        bottomEnd = 0.dp
                    )
                    // right outer button
                    representationTypes.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = cornerRadius
                    )
                    // middle button
                    else -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                },
                border = BorderStroke(
                    1.dp, if (currentType == itemRepresentationType) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.75f)
                    }
                ),
                colors = if (currentType == itemRepresentationType) {
                    // selected colors
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colors.primary
                    )
                } else {
                    // not selected colors
                    ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        contentColor = MaterialTheme.colors.primary
                    )
                },
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    painter = when (itemRepresentationType) {
                        ItemRepresentationType.Card -> painterResource("vector/view_agenda_black_24dp.svg")
                        ItemRepresentationType.Table -> painterResource("vector/grid_view_black_24dp.svg")
                    },
                    contentDescription = "Representation selector",
                    tint = if (currentType == itemRepresentationType) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.DarkGray.copy(alpha = 0.9f)
                    }
                )
            }
        }
    }
}