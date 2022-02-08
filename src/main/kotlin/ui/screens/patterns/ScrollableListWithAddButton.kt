package ui.screens.patterns

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import ui.composable.ScrollableBox

@Composable
fun ScrollableListWithAddButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    scrollState: ScrollState = rememberScrollState(),
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {

        ScrollableBox(
            modifier = modifier.fillMaxHeight(), scrollState = scrollState, innerHorizontalPadding = 64.dp,
            content = content
        )

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .alpha(
                    if (scrollState.isScrollInProgress) 0.25f else 1f
                ),
            onClick = onAddClick,
            content = {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add button")
            }
        )
    }
}