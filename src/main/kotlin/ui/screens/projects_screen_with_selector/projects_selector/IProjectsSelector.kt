package ui.screens.projects_screen_with_selector.projects_selector

import com.arkivanov.decompose.value.Value

interface IProjectsSelector {
    //current state of the screen
    val state: Value<Model>

    //methods to change the state:
    //set selected item:
    fun selectProject(projectID: String)

    data class Model(val projects: List<SelectorItem>, val selectedItem: SelectorItem?)

    data class SelectorItem(val projectID: String, val projectName: String)
}