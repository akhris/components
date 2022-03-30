package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Container

typealias    GetContainer = GetEntity<String, Container>
typealias InsertContainer = InsertEntity<String, Container>
typealias RemoveContainer = RemoveEntity<String, Container>
typealias UpdateContainer = UpdateEntity<String, Container>
typealias GetContainersList = GetEntities<String, Container>
typealias GetContainersGroupedList = GetGroupedEntities<String, Container>
