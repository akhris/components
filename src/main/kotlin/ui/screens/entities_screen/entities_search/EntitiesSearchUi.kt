package ui.screens.entities_screen.entities_search

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.delay

@Composable
fun EntitiesSearchUi(component: IEntitiesSearch) {
    val state by remember(component) { component.state }.subscribeAsState()

    var searchString by remember { mutableStateOf(state.searchString) }

    OutlinedTextField(
        value = searchString,
        onValueChange = {
            searchString = it
        },
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = "search string")
        },
        trailingIcon = if (searchString.isNotEmpty()) {
            {
                ui.composable.Icons.ClearIcon(modifier = Modifier.clickable {
                    searchString = ""
                })
            }
        } else null,
        label = {
            Text("search")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Unspecified,
            unfocusedBorderColor = Color.Unspecified,
            errorBorderColor = Color.Unspecified,
            disabledBorderColor = Color.Unspecified
        )
    )

    //debounce:
    LaunchedEffect(searchString) {
        if (searchString == state.searchString) {
            return@LaunchedEffect
        }
        //when count changes:
        delay(500L)
        component.setSearchString(searchString)
    }
}