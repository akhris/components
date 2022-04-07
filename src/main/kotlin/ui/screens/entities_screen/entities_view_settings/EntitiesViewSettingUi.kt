package ui.screens.entities_screen.entities_view_settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState


@Composable
fun EntitiesViewSettingsUi(component: IEntitiesViewSettings) {
    val state by remember(component) { component.state }.subscribeAsState()
    RepresentationTypesSelector(
        representationTypes = state.representationTypes,
        currentType = state.currentType,
        onTypeChanged = component::onRepresentationTypeChanged
    )
}

@Composable
private fun RepresentationTypesSelector(
    representationTypes: List<ItemRepresentationType> = listOf(
        ItemRepresentationType.Card,
        ItemRepresentationType.Table
    ),
    currentType: ItemRepresentationType,
    onTypeChanged: (ItemRepresentationType) -> Unit
) {
    val cornerRadius = remember { 8.dp }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
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

