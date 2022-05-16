package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Parameter

typealias GetParameter = GetEntity<String, Parameter>
typealias InsertParameter = InsertEntity<String, Parameter>
typealias RemoveParameter = RemoveEntity<String, Parameter>
typealias UpdateParameter = UpdateEntity<String, Parameter>
typealias GetParametersList = GetListItemsUseCase<String, Parameter>