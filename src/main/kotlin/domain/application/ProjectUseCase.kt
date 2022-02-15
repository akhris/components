package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Project

typealias    GetProject = GetEntity<String, Project>
typealias InsertProject = InsertEntity<String, Project>
typealias RemoveProject = RemoveEntity<String, Project>
typealias UpdateProject = UpdateEntity<String, Project>
typealias GetProjectsList = GetEntities<String, Project>
