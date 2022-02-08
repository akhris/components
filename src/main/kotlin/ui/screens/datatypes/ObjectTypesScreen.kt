package ui.screens.datatypes

import androidx.compose.runtime.*
import com.akhris.domain.core.utils.IDUtils
import domain.entities.ObjectType
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.EditEntityDialog
import ui.screens.EntityScreenContent
import viewmodels.MultipleEntitiesViewModel

@Composable
fun ObjectTypesScreen(showAddDialog: MutableState<Boolean>? = null) {
    val di = localDI()
    val viewModel: MultipleEntitiesViewModel<String, ObjectType> by di.instance()
    val entities by remember { viewModel.entities }.collectAsState()

    EntityScreenContent(entities, onEntityRemoved = {
        viewModel.removeEntity(it)
    })

    if (showAddDialog?.value == true) {
        EditEntityDialog(
            title = "Add new ${ObjectType::class.simpleName}",
            entity = ObjectType(id = IDUtils.newID()),
            onSaveClicked = {
                viewModel.insertEntity(it)
            },
            onDismiss = {
                showAddDialog.value = false
            }
        )
    }
}

