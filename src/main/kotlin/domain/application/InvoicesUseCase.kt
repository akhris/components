package domain.application

import com.akhris.domain.core.application.GetEntity
import com.akhris.domain.core.application.InsertEntity
import com.akhris.domain.core.application.RemoveEntity
import com.akhris.domain.core.application.UpdateEntity
import domain.entities.Invoice

typealias    GetInvoice = GetEntity<String, Invoice>
typealias InsertInvoice = InsertEntity<String, Invoice>
typealias RemoveInvoice = RemoveEntity<String, Invoice>
typealias UpdateInvoice = UpdateEntity<String, Invoice>
typealias GetInvoicesList = GetListItemsUseCase<String, Invoice>