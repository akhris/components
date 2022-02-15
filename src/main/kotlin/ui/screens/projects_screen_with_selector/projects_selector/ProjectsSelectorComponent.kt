package ui.screens.projects_screen_with_selector.projects_selector

import com.akhris.domain.core.application.GetEntities
import com.akhris.domain.core.utils.unpack
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.application.GetProjectsList
import kotlinx.coroutines.*
import persistence.repository.Specification

class ProjectsSelectorComponent(
    componentContext: ComponentContext,
    private val onProjectSelected: (String?) -> Unit,
    private val getProjectsList: GetProjectsList
) :
    IProjectsSelector,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())


    private val _state = MutableValue(IProjectsSelector.Model(projects = listOf(), selectedItem = null))

    override val state: Value<IProjectsSelector.Model> = _state


    override fun selectProject(projectID: String) {
        _state.reduce { s ->
            s.copy(selectedItem = s.projects.find { it.projectID == projectID })
        }
        onProjectSelected(projectID)
    }


    private fun invalidateProjectsList() {
        scope.launch {
            val projects = getProjectsList(GetEntities.GetBySpecification(Specification.QueryAll)).unpack()
            _state.reduce {
                it.copy(
                    projects = projects.map { p ->
                        IProjectsSelector.SelectorItem(
                            projectID = p.id,
                            projectName = p.name
                        )
                    }
                )
            }
        }
    }

    init {
        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })

        invalidateProjectsList()

    }

}