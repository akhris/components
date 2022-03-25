package ui.entity_renderers

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.entities.IEntity
import domain.entities.usecase_factories.IInsertUseCaseFactory
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.theme.DialogSettings

@Composable
fun <T : IEntity<*>> AddEntityDialog(entity: T, onDismiss: () -> Unit) {
    val di = localDI()
    val insertFactory by di.instance<IInsertUseCaseFactory>()
    val insertUseCase = remember(entity, insertFactory) { insertFactory.getInsertUseCase(entityClass = entity::class) }

    val state = rememberDialogState(
        size = DpSize(
            width = DialogSettings.defaultWideDialogWidth,
            height = DialogSettings.defaultWideDialogHeight
        )
    )

    var entityToSave by remember { mutableStateOf<T?>(null) }

    Dialog(
        state = state,
        title = "add new ${entity::class.simpleName}",
        onCloseRequest = onDismiss,
        content = {
            Surface {
                BoxWithConstraints {
                    RenderCardEntity(
                        initialEntity = entity,
                        onEntitySaveClicked = {
                            entityToSave = it
                        }
                    )
                }
            }
        })

    LaunchedEffect(entityToSave) {
        entityToSave?.let {
            insertUseCase?.invoke(InsertEntity.Insert(it))
            onDismiss()
        }
    }
}