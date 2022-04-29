package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Supplier

typealias    GetSupplier = GetEntity<String, Supplier>
typealias InsertSupplier = InsertEntity<String, Supplier>
typealias RemoveSupplier = RemoveEntity<String, Supplier>
typealias UpdateSupplier = UpdateEntity<String, Supplier>
typealias GetSuppliersList = GetListItemsUseCase<String, Supplier>