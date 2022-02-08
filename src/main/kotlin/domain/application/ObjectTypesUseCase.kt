package domain.application

import com.akhris.domain.core.application.*
import domain.entities.ObjectType

typealias GetObjectType = GetEntity<String, ObjectType>
typealias InsertObjectType = InsertEntity<String, ObjectType>
typealias RemoveObjectType = RemoveEntity<String, ObjectType>
typealias UpdateObjectType = UpdateEntity<String, ObjectType>
typealias GetObjectTypes = GetEntities<String, ObjectType>
