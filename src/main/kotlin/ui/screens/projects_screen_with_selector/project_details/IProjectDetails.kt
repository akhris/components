package ui.screens.projects_screen_with_selector.project_details

import com.arkivanov.decompose.value.Value
import domain.entities.Project

interface IProjectDetails {
    //current state of the screen
    val state: Value<Model>

    //entities list model
    data class Model(
        val project: Project? = null
    )
}