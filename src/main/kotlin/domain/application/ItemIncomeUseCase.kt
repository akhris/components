package domain.application

import com.akhris.domain.core.application.*
import domain.entities.ItemIncome

typealias       GetItemIncome = GetEntity<String, ItemIncome>
typealias InsertItemIncome = InsertEntity<String, ItemIncome>
typealias RemoveItemIncome = RemoveEntity<String, ItemIncome>
typealias UpdateItemIncome = UpdateEntity<String, ItemIncome>
typealias GetItemIncomesList = GetEntities<String, ItemIncome>