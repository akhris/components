package domain.application

import com.akhris.domain.core.application.*
import domain.entities.Invoice

typealias    GetInvoice = GetEntity<String, Invoice>
typealias InsertInvoice = InsertEntity<String, Invoice>
typealias RemoveInvoice = RemoveEntity<String, Invoice>
typealias UpdateInvoice = UpdateEntity<String, Invoice>
typealias GetInvoicesList = GetEntities<String, Invoice>