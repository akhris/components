package persistence.repository

import domain.entities.ObjectType
import domain.repository.ITypesRepository

class ObjectTypesTestRepository : BaseEntityTestRepository<String, ObjectType>(), ITypesRepository {


}