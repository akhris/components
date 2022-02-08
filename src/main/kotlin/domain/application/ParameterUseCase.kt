package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Parameter

typealias GetParameter = GetEntity<String, Parameter>
typealias InsertParameter = InsertEntity<String, Parameter>
typealias RemoveParameter = RemoveEntity<String, Parameter>
typealias UpdateParameter = UpdateEntity<String, Parameter>
typealias GetParametersList = GetEntities<String, Parameter>