package ui.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

object Icons {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ClearIcon(modifier: Modifier) {
        var isHover by remember { mutableStateOf(false) }
        val iconTint by animateColorAsState(if (isHover) MaterialTheme.colors.error else MaterialTheme.colors.onSurface.copy(0.1f))

        Icon(
            modifier = modifier.onPointerEvent(PointerEventType.Enter) { isHover = true }
                .onPointerEvent(PointerEventType.Exit) { isHover = false },
            imageVector = Icons.Rounded.Clear,
            contentDescription = "clear",
            tint = iconTint
        )
    }
}