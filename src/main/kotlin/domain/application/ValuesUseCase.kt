package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Value

typealias        GetValue = GetEntity<String, Value>
typealias  InsertValue = InsertEntity<String, Value>
typealias  RemoveValue = RemoveEntity<String, Value>
typealias  UpdateValue = UpdateEntity<String, Value>
typealias GetValuesList = GetEntities<String, Value>
