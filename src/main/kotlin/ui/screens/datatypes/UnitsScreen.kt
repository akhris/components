package ui.screens.datatypes

import androidx.compose.runtime.*
import com.akhris.domain.core.utils.IDUtils
import domain.entities.Unit
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ui.screens.EditEntityDialog
import ui.screens.EntityScreenContent
import viewmodels.MultipleEntitiesViewModel

@Composable
fun UnitsScreen(showAddDialog: MutableState<Boolean>? = null) {
    val di = localDI()
    val viewModel: MultipleEntitiesViewModel<String, Unit> by di.instance()
    val entities by remember { viewModel.entities }.collectAsState()
    EntityScreenContent(entities, onEntityRemoved = {
        viewModel.removeEntity(it)
    })

    if (showAddDialog?.value == true) {
        EditEntityDialog(
            title = "Add new ${Unit::class.simpleName}",
            entity = Unit(id = IDUtils.newID()),
            onSaveClicked = {
                viewModel.insertEntity(it)
            },
            onDismiss = {
                showAddDialog.value = false
            }
        )
    }
}

