package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.entities.ObjectType
import test.CapacitorTestEntities
import test.ResistorTestEntities
import ui.theme.ContentSettings

@Composable
fun ObjectTypesScreen() {
    ObjectTypesScreenContent(listOf(ResistorTestEntities.resistorsType, CapacitorTestEntities.capacitorsType))
}

@Composable
fun ObjectTypesScreenContent(items: List<ObjectType>) {
    Column {
        items.forEach {
            ObjectTypeItem(it)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ObjectTypeItem(objectType: ObjectType) {
    Card(
        modifier = Modifier.fillMaxWidth().height(
            height = ContentSettings.contentCardHeight
        )
            .padding(ContentSettings.contentHorizontalPadding)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //title:
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = objectType.name,
                    style = MaterialTheme.typography.h6
                )
            }
            //params:
            Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                objectType.parameters.forEach { parameter ->
                    ListItem(
                        secondaryText = { Text(text = parameter.description) },
                        text = { Text(text = parameter.name) },
                        trailing = parameter.unit?.let {
                            {
                                Text(text = it.unit)
                            }
                        })
                }
            }
        }
    }
}
