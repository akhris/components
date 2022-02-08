package persistence.repository

import domain.entities.Container
import domain.repository.IContainersRepository

class ContainersTestRepository : BaseEntityTestRepository<String, Container>(), IContainersRepository