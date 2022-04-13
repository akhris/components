package persistence.datasources.exposed

import domain.entities.Parameter
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityParameter
import persistence.dto.exposed.Tables
import persistence.mappers.toParameter
import utils.set
import utils.toUUID

class ParametersDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Parameter, EntityParameter, Tables.Parameters>(
    table = Tables.Parameters,
    entityClass = EntityParameter,
    columnMappersFactory.getColumnMapper(Parameter::class)
) {
    override fun mapToEntity(exposedEntity: EntityParameter): Parameter = exposedEntity.toParameter()

    override fun insertStatement(entity: Parameter): Tables.Parameters.(InsertStatement<Number>) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[unit] = entity.unit?.id?.toUUID()
    }

    override fun updateStatement(entity: Parameter): Tables.Parameters.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[description] = entity.description
        it[unit] = entity.unit?.id?.toUUID()
    }


}


/*
   override suspend fun getByID(id: String): Parameter? {
       return newSuspendedTransaction {
           addLogger(StdOutSqlLogger)
           try {
               EntityParameter.get(id = UUID.fromString(id)).toParameter()
           } catch (e: Exception) {
               log(e)
               null
           }
       }
   }

   override suspend fun getAll(filters: List<FilterSpec>): List<Parameter> {
       return newSuspendedTransaction {
           addLogger(StdOutSqlLogger)
           EntityParameter.all().map { it.toParameter() }
       }
   }

   override suspend fun insert(entity: Parameter) {
       newSuspendedTransaction {
           addLogger(StdOutSqlLogger)
           Tables.Parameters.insert { statement ->
               statement[id] = entity.id.toUUID()
               statement[name] = entity.name
               statement[description] = entity.description
               entity.unit?.id?.toUUID()?.let {
                   statement[unit] = it
               }
           }
           commit()
       }
   }

   override suspend fun update(entity: Parameter) {
       newSuspendedTransaction {
           addLogger(StdOutSqlLogger)

           //1. get entity by id:
           val parameter = EntityParameter[entity.id.toUUID()]

           //2. update it:
           parameter.name = entity.name
           parameter.description = entity.description
           parameter.unit = entity.unit?.id?.let {
               EntityUnit[it.toUUID()]
           }
           commit()
       }
   }

   override suspend fun removeById(id: String) {
       newSuspendedTransaction {
           addLogger(StdOutSqlLogger)

           //1. get entity by id:
           val parameter = EntityParameter[id.toUUID()]

           //2. remove it:
           parameter.delete()
           commit()
       }
   }

    */
