package domain.application

import com.akhris.domain.core.application.*
import domain.entities.ItemOutcome

typealias    GetItemOutcome =    GetEntity<String, ItemOutcome>
typealias InsertItemOutcome = InsertEntity<String, ItemOutcome>
typealias RemoveItemOutcome = RemoveEntity<String, ItemOutcome>
typealias UpdateItemOutcome = UpdateEntity<String, ItemOutcome>
typealias GetItemOutcomesList =GetEntities<String, ItemOutcome>