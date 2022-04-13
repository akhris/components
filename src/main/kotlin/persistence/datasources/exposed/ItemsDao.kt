package persistence.datasources.exposed

import domain.entities.Item
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import persistence.columnMappers.ColumnMappersFactory
import persistence.dto.exposed.EntityItem
import persistence.dto.exposed.Tables
import persistence.mappers.toItem
import utils.set
import utils.toUUID

class ItemsDao(columnMappersFactory: ColumnMappersFactory) : BaseUUIDDao<Item, EntityItem, Tables.Items>(
    table = Tables.Items,
    entityClass = EntityItem,
    columnMappersFactory.getColumnMapper(Item::class)
) {


    override fun mapToEntity(exposedEntity: EntityItem): Item {
        return exposedEntity.toItem()
    }

    override fun insertStatement(entity: Item): Tables.Items.(InsertStatement<Number>) -> Unit = { statement ->
        statement[name] = entity.name
        statement[type] = entity.type?.id?.toUUID()
    }

    override fun Transaction.doAfterInsert(entity: Item) {
        Tables.ItemValues.batchInsert(entity.values) { v ->
            this[Tables.ItemValues.item] = entity.id.toUUID()
            this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
            this[Tables.ItemValues.value] = v.value
            this[Tables.ItemValues.factor] = v.factor
        }
    }

    override fun updateStatement(entity: Item): Tables.Items.(UpdateStatement) -> Unit = { statement ->
        statement[name] = entity.name
        statement[type] = entity.type?.id?.toUUID()
    }

    override fun Transaction.doAfterUpdate(entity: Item) {
        //3. update values list:
        //remove all old values
        Tables
            .ItemValues
            .deleteWhere { Tables.ItemValues.item eq entity.id.toUUID() }

        //batch insert all new values
        Tables
            .ItemValues
            .batchInsert(entity.values) { v ->
                this[Tables.ItemValues.item] = entity.id.toUUID()
                this[Tables.ItemValues.value] = v.value
                this[Tables.ItemValues.factor] = v.factor
                this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
            }
    }
}


/*
  override suspend fun getByID(id: String): Item? {
      return try {
          newSuspendedTransaction {
              addLogger(StdOutSqlLogger)
              EntityItem.get(id = UUID.fromString(id)).toItem()
          }
      } catch (e: Exception) {
          log(e)
          null
      }
  }

  override suspend fun query(
      filterSpec: ISpecification?,
      sortingSpec: ISpecification?,
      pagingSpec: ISpecification?
  ): List<Item> {
      TODO("Not yet implemented")
  }

  override suspend fun getItemsCount(
      filterSpec: ISpecification?,
      sortingSpec: ISpecification?,
      pagingSpec: ISpecification?
  ): Long {
      TODO("Not yet implemented")
  }

  override suspend fun insert(entity: Item) {
      newSuspendedTransaction {
          addLogger(StdOutSqlLogger)
          Tables.Items.insert { statement ->
              statement[id] = entity.id.toUUID()
              statement[name] = entity.name
              entity.type?.id?.toUUID()?.let {
                  statement[type] = it
              }
          }

          Tables.ItemValues.batchInsert(entity.values) { v ->
              this[Tables.ItemValues.item] = entity.id.toUUID()
              this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
              this[Tables.ItemValues.value] = v.value
              this[Tables.ItemValues.factor] = v.factor
          }
          commit()
      }
  }

  override suspend fun update(entity: Item) {
      newSuspendedTransaction {
          addLogger(StdOutSqlLogger)

          Tables
              .Items
              .update({ Tables.Items.id eq entity.id.toUUID() }) { statement ->
                  statement[Tables.Items.name] = entity.name
                  statement[Tables.Items.type] = entity.type?.id?.toUUID()
              }

          //3. update values list:
          //remove all old values
          Tables
              .ItemValues
              .deleteWhere { Tables.ItemValues.item eq entity.id.toUUID() }

          //batch insert all new values
          Tables
              .ItemValues
              .batchInsert(entity.values) { v ->
                  this[Tables.ItemValues.item] = entity.id.toUUID()
                  this[Tables.ItemValues.value] = v.value
                  this[Tables.ItemValues.factor] = v.factor
                  this[Tables.ItemValues.parameter] = v.entity.id.toUUID()
              }
          commit()
      }
  }

  override suspend fun removeById(id: String) {
      newSuspendedTransaction {
          addLogger(StdOutSqlLogger)
          EntityItem[id.toUUID()].delete()
          commit()
      }
  }

   */