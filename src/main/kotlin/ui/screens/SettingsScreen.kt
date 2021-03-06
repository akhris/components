package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import settings.AppSettingsRepository
import strings.Strings
import ui.composable.ScrollableBox
import utils.toLocalizedString
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter
import kotlin.io.path.Path
import kotlin.io.path.extension

@Composable
fun SettingsScreen() {
    val di = localDI()
    val settingsRepository: AppSettingsRepository by di.instance()

    SettingsScreenContent(settingsRepository)
}

@Composable
fun SettingsScreenContent(settingsRepository: AppSettingsRepository) {




    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //title row
        Surface(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = Strings.navitem_settings_title.toLocalizedString(),
                style = MaterialTheme.typography.h3
            )
        }
        ScrollableBox(modifier = Modifier.weight(1f).padding(horizontal = 32.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(32.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                val isLightTheme by remember(settingsRepository) { settingsRepository.isLightTheme }.collectAsState(null)
                val dbLocation by remember(settingsRepository) { settingsRepository.dbLocation }.collectAsState(null)


                DatabaseLocationPicker(dbLocation ?: "") {
                    settingsRepository.setDBLocation(it)
                }

                LightDarkThemeSelector(isLightTheme ?: true) {
                    settingsRepository.setIsLightTheme(it)
                }
            }
        }
    }
}

@Composable
private fun DatabaseLocationPicker(location: String, onLocationChanged: (String) -> Unit) {
    PathPreference(location, description = "database location", onPathChanged = onLocationChanged)
}


@Composable
private fun LightDarkThemeSelector(isLight: Boolean, onThemeChanged: (Boolean) -> Unit) {

    SwitchPreference(
        isChecked = !isLight,
        onCheckedChange = { onThemeChanged(!it) },
        title = "Dark mode",
        description = "Toggle theme colors to light/dark mode",
        iconPath = "vector/dark_mode_black_24dp.svg"
    )
}


@Composable
private fun PathPreference(path: String, description: String? = null, onPathChanged: (String) -> Unit) {

    var showFileDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        TextField(
            label = description?.let {
                {
                    Text(text = it)
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    showFileDialog = true
                }, content = {
                    Icon(painterResource("vector/folder_black_24dp.svg"), contentDescription = "open path picker")
                })
            },
            value = path,
            onValueChange = onPathChanged
        )
    }

    if (showFileDialog) {
        FileDialog(initDir = path, onCloseRequest = {
            it?.let { onPathChanged(it) }
            showFileDialog = false
        })
    }
}


@Composable
private fun FileDialog(
    initDir: String? = null,
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {

            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    if (directory != null && file != null)
                        onCloseRequest(Path(directory, file).toString())
                }
            }

        }
    },
    dispose = FileDialog::dispose,
    update = { dialog ->
        dialog.filenameFilter = FilenameFilter { dir, name ->
            Path(name).extension == "db"
        }

        initDir?.let {
            dialog.directory = Path(it).parent.toString()
        }

    }
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwitchPreference(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String = "",
    description: String? = null,
    iconPath: String? = null
) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        ListItem(text =
        {
            Text(text = title)
        }, secondaryText = description?.let {
            {
                Text(text = it)
            }
        }, trailing = {
            Switch(checked = isChecked, onCheckedChange = onCheckedChange)
        }, icon = iconPath?.let {
            {
                Icon(painter = painterResource(it), contentDescription = "switch preference icon")
            }
        })
    }
}
