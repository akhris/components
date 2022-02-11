package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Supplier

typealias    GetSupplier = GetEntity<String, Supplier>
typealias InsertSupplier = InsertEntity<String, Supplier>
typealias RemoveSupplier = RemoveEntity<String, Supplier>
typealias UpdateSupplier = UpdateEntity<String, Supplier>
typealias GetSuppliersList = GetEntities<String, Supplier>