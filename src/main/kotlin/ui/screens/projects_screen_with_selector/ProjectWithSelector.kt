package ui.screens.projects_screen_with_selector

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.application.GetProject
import domain.application.GetProjectsList
import domain.entities.Project
import domain.entities.usecase_factories.IGetListUseCaseFactory
import domain.entities.usecase_factories.IGetUseCaseFactory
import ui.screens.projects_screen_with_selector.project_details.ProjectDetailsComponent
import ui.screens.projects_screen_with_selector.projects_selector.ProjectsSelectorComponent


class ProjectWithSelector(
    componentContext: ComponentContext,
    private val getListUseCaseFactory: IGetListUseCaseFactory,
    private val getUseCaseFactory: IGetUseCaseFactory
) : IEntitiesWithSelector,
    ComponentContext by componentContext {


    private val listRouter: Router<ProjectDetailsConfig, IEntitiesWithSelector.ListChild> =
        router(
            initialConfiguration = ProjectDetailsConfig.ProjectDetails(),
            key = "list_router",
            childFactory = ::createListChild
        )

    private val selectorRouter =
        router(
            initialConfiguration = ProjectsSelectorConfig.Selector,
            key = "filter_router",
            handleBackButton = false, // Pop the back stack on back button press
            childFactory = ::createSelectorChild
        )

    override val listRouterState: Value<RouterState<*, IEntitiesWithSelector.ListChild>> = listRouter.state
    override val selectorRouterState: Value<RouterState<*, IEntitiesWithSelector.SelectorChild>> = selectorRouter.state


    private fun createSelectorChild(
        projectsFilterConfig: ProjectsSelectorConfig,
        componentContext: ComponentContext
    ): IEntitiesWithSelector.SelectorChild {
        return when (projectsFilterConfig) {
            ProjectsSelectorConfig.Selector -> IEntitiesWithSelector.SelectorChild.Selector(
                ProjectsSelectorComponent(
                    componentContext = componentContext,
                    getProjectsList = getListUseCaseFactory.getListUseCase(Project::class) as GetProjectsList,
                    onProjectSelected = { selectedID ->
                        listRouter.navigate { stack ->
                            stack
                                .dropLastWhile { it is ProjectDetailsConfig.ProjectDetails }
                                .plus(ProjectDetailsConfig.ProjectDetails(selectedID))
                        }
                    }
                )
            )
        }
    }

    private fun createListChild(
        projectDetailsConfig: ProjectDetailsConfig,
        componentContext: ComponentContext
    ): IEntitiesWithSelector.ListChild {
        return when (projectDetailsConfig) {
            is ProjectDetailsConfig.ProjectDetails -> IEntitiesWithSelector.ListChild.List(
                ProjectDetailsComponent(
                    componentContext = componentContext,
                    projectID = projectDetailsConfig.selectedProject,
                    getProject = getUseCaseFactory.getUseCase(Project::class) as GetProject
                )
            )
        }
    }


    sealed class ProjectDetailsConfig : Parcelable {
        @Parcelize
        data class ProjectDetails(val selectedProject: String? = null) : ProjectDetailsConfig()
    }

    sealed class ProjectsSelectorConfig : Parcelable {
        @Parcelize
        object Selector : ProjectsSelectorConfig()
    }


}