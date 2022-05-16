package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Container

typealias    GetContainer = GetEntity<String, Container>
typealias InsertContainer = InsertEntity<String, Container>
typealias RemoveContainer = RemoveEntity<String, Container>
typealias UpdateContainer = UpdateEntity<String, Container>
typealias GetContainersList = GetListItemsUseCase<String, Container>
