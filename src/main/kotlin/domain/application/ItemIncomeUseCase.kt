package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.ItemIncome

typealias       GetItemIncome = GetEntity<String, ItemIncome>
typealias InsertItemIncome = InsertEntity<String, ItemIncome>
typealias RemoveItemIncome = RemoveEntity<String, ItemIncome>
typealias UpdateItemIncome = UpdateEntity<String, ItemIncome>
typealias GetItemIncomesList = GetListItemsUseCase<String, ItemIncome>