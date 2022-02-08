package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Unit

typealias GetUnit = GetEntity<String, Unit>
typealias InsertUnit = InsertEntity<String, Unit>
typealias RemoveUnit = RemoveEntity<String, Unit>
typealias UpdateUnit = UpdateEntity<String, Unit>
typealias GetUnits = GetEntities<String, Unit>