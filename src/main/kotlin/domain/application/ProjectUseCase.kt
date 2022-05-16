package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Project

typealias    GetProject = GetEntity<String, Project>
typealias InsertProject = InsertEntity<String, Project>
typealias RemoveProject = RemoveEntity<String, Project>
typealias UpdateProject = UpdateEntity<String, Project>
typealias GetProjectsList = GetListItemsUseCase<String, Project>
