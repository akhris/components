package ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import ui.theme.DialogSettings
import java.time.LocalDateTime

@Composable
fun TimePickerDialog(
    initialTime: LocalDateTime? = null,
    onDismiss: () -> Unit,
    onTimeSelected: (LocalDateTime) -> Unit
) {

    val dialogState = rememberDialogState(
        size = DpSize(
            width = DialogSettings.TimePickerSettings.defaultPickerWidth,
            height = DialogSettings.TimePickerSettings.defaultPickerInputModeHeight
        )
    )

    Dialog(
        state = dialogState,
        onCloseRequest = onDismiss,
        undecorated = true,
        resizable = false,
        transparent = true,
        content = {
            TimePickerDialogContent(
                initialTime = initialTime,
                onCancelClick = onDismiss,
                onOkClick = {
                    onTimeSelected(it)
                    onDismiss()
                }
            )
        }
    )
}

@Composable
private fun TimePickerDialogContent(
    initialTime: LocalDateTime? = null,
    onCancelClick: (() -> Unit)? = null,
    onOkClick: ((LocalDateTime) -> Unit)? = null
) {

}