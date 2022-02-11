package ui.screens.types_of_data

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfade
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import ui.screens.patterns.ScreenWithFilterSheet
import ui.screens.types_of_data.data_types_list.DataTypesListUi
import ui.screens.types_of_data.types_selector.TypesSelectorUi
import utils.toLocalizedString

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TypesOfDataUi(component: ITypesOfData) {
    val selectedItem by component.selectedItem.subscribeAsState()
    ScreenWithFilterSheet(
        mainScreenTitle = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 2.dp),
                    text = {
                        Text(
                            text = selectedItem.name?.toLocalizedString() ?: "",
                            style = MaterialTheme.typography.h3
                        )
                    },
                    secondaryText = {
                        Text(text = selectedItem.description?.toLocalizedString() ?: "")
                    }
                )
            }
        },
        isOpened = true,
        isModal = true,
        content = {
            ListPane(component.listRouterState)
//            DataTypesListUi(component = dataTypesListComponent)
        },
        filterContent = {
            FilterPane(component.filterRouterState)
//            TypesSelectorUi(typesSelectorComponent = typesSelectorComponent)
        }
    )

}

@Composable
fun ListPane(routerState: Value<RouterState<*, ITypesOfData.ListChild>>) {
    Children(routerState) {
        when (val child = it.instance) {
            is ITypesOfData.ListChild.List -> {
                DataTypesListUi(component = child.component)
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun FilterPane(routerState: Value<RouterState<*, ITypesOfData.FilterChild>>) {
    Children(routerState, animation = crossfade()) {
        when (val child = it.instance) {
            is ITypesOfData.FilterChild.Filter -> {
                TypesSelectorUi(child.component)
            }
//            ITypesOfData.FilterChild.None -> {
//
//            }
        }
    }
}