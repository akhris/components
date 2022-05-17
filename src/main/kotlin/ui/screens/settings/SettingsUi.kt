package ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import settings.AppSetting
import settings.AppSettingsRepository
import strings.LocalizedStrings
import strings.StringsIDs
import strings.defaultLocalizedStrings
import ui.composable.ScrollableBox
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter
import kotlin.io.path.Path
import kotlin.io.path.extension

@Composable
fun SettingsUi(settingsComponent: ISettings, localizedStrings: LocalizedStrings = defaultLocalizedStrings) {

    val settings by settingsComponent.state.collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //title row
        Surface(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = localizedStrings(StringsIDs.navitem_settings_title),
                style = MaterialTheme.typography.h3
            )
        }
        ScrollableBox(modifier = Modifier.weight(1f).padding(horizontal = 32.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(32.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                settings?.settings?.forEach {
                    when (val s = it.setting) {
                        is AppSetting.BooleanSetting -> {
                            SwitchPreference(
                                isChecked = s.value,
                                onCheckedChange = { newValue -> settingsComponent.onSettingChanged(s.copy(value = newValue)) },
                                title = it.name,
                                description = it.description,
                                iconPath = it.iconPath
                            )
                        }
                        is AppSetting.FloatSetting -> {

                        }
                        is AppSetting.StringSetting -> {

                        }
                        is AppSetting.PathSetting -> {
                            PathPreference(
                                s.value ?: "",
                                description = it.name,
                                isRestartNeeded = it.setting.key == AppSettingsRepository.key_db_location,
                                onPathChanged = { newPath -> settingsComponent.onSettingChanged(s.copy(value = newPath)) })
                        }
                        is AppSetting.ListSetting -> {
                            ListPreference(
                                description = it.name,
                                valuesMap = s.values,
                                selectedKey = s.selectedKey,
                                onKeyChanged = { newKey ->
                                    settingsComponent.onSettingChanged(s.copy(selectedKey = newKey))
                                }
                            )
                        }
                    }
                }
            }
        }
        //app version label:
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp),
            text = "version • 1.0.0b",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
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
private fun PathPreference(
    path: String,
    description: String? = null,
    isRestartNeeded: Boolean = false,
    onPathChanged: (String) -> Unit
) {

    var showFileDialog by remember { mutableStateOf(false) }
    val initialPath by remember { mutableStateOf(path) }

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        Column {
            TextField(
                modifier = Modifier.fillMaxWidth(),
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
            if (isRestartNeeded && initialPath != path) {
                //show restart needed warning
                Text(
                    modifier = Modifier.align(Alignment.End).padding(4.dp),
                    text = "restart app to take effect",
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold)
                )

            }
        }
    }

    if (showFileDialog) {
        FileDialog(initDir = path, onCloseRequest = {
            it?.let { onPathChanged(it) }
            showFileDialog = false
        })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListPreference(
    description: String? = null,
    valuesMap: Map<String, String>,
    selectedKey: String,
    onKeyChanged: (String) -> Unit
) {

    var showDropDown by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
        ListItem(
            modifier = Modifier.clickable {
                showDropDown = !showDropDown
            },
            text = {
                Text(valuesMap[selectedKey] ?: "")
            }, overlineText = description?.let {
                {
                    Text(it)
                }
            }, trailing = {
                IconButton(
                    modifier = Modifier.rotate(if (showDropDown) 180f else 0f),
                    content = {
                        Icon(
                            Icons.Rounded.ArrowDropDown,
                            contentDescription = "show dropdown menu"
                        )
                    },
                    onClick = { showDropDown = !showDropDown })
            })
    }

    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = showDropDown,
        onDismissRequest = { showDropDown = false },
        content = {
            valuesMap.forEach { (key, value) ->
                val background =
                    if (key == selectedKey) MaterialTheme.colors.primarySurface else MaterialTheme.colors.background
                DropdownMenuItem(content = {
                    Surface(color = background) {
                        ListItem(
                            singleLineSecondaryText = true,
                            text = { Text(value) },
                            secondaryText = { Text(key) })
                    }
                }, onClick = {
                    onKeyChanged(key)
                    showDropDown = false
                })
            }
        })

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
