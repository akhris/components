package persistence.repository

import domain.entities.Unit
import domain.repository.IUnitsRepository

class UnitsTestRepository : BaseEntityTestRepository<String, Unit>(), IUnitsRepository {


}