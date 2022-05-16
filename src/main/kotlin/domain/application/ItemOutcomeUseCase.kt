package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.ItemOutcome

typealias    GetItemOutcome =    GetEntity<String, ItemOutcome>
typealias InsertItemOutcome = InsertEntity<String, ItemOutcome>
typealias RemoveItemOutcome = RemoveEntity<String, ItemOutcome>
typealias UpdateItemOutcome = UpdateEntity<String, ItemOutcome>
typealias GetItemOutcomesList =GetListItemsUseCase<String, ItemOutcome>