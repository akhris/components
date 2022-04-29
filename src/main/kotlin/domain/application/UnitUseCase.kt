package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Unit

typealias GetUnit = GetEntity<String, Unit>
typealias InsertUnit = InsertEntity<String, Unit>
typealias RemoveUnit = RemoveEntity<String, Unit>
typealias UpdateUnit = UpdateEntity<String, Unit>
typealias GetUnits = GetListItemsUseCase<String, Unit>