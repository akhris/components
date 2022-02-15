package ui.screens.projects_screen_with_selector.project_details

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.utils.unpack
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.arkivanov.essenty.lifecycle.subscribe
import domain.application.GetProject
import kotlinx.coroutines.*

class ProjectDetailsComponent(
    componentContext: ComponentContext,
    private val getProject: GetProject,
    private val projectID: String?
) : IProjectDetails,
    ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _state = MutableValue(IProjectDetails.Model())

    override val state: Value<IProjectDetails.Model> = _state

    private fun invalidateEntitiesList() {
        scope.launch {
            val project = projectID?.let { getProject(GetEntity.GetByID(id = it)).unpack() }
            project?.let { p ->
                _state.reduce {
                    it.copy(project = p)
                }
            }
        }
    }

    init {
        lifecycle.subscribe(onDestroy = {
            scope.coroutineContext.cancelChildren()
        })

        invalidateEntitiesList()
    }


}