package ui.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableBox(
    modifier: Modifier = Modifier,
    innerHorizontalPadding: Dp = 0.dp,
    scrollState: ScrollState = rememberScrollState(),
    additionalContent: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier) {
        Box(modifier = Modifier.verticalScroll(scrollState).padding(horizontal = innerHorizontalPadding)) {
            content()
        }

        additionalContent?.invoke(this)

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )
    }
}