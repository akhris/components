package persistence.repository

import domain.entities.Parameter
import domain.repository.IParametersRepository

class ParametersTestRepository : BaseEntityTestRepository<String, Parameter>(), IParametersRepository {

}