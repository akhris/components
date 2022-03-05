package persistence.mappers

import com.akhris.domain.core.mappers.IMapper
import com.akhris.domain.core.mappers.Mapper
import domain.entities.ItemIncome
import domain.entities.Unit
import org.jetbrains.exposed.dao.id.EntityID
import persistence.dto.exposed.EntityItemIncome
import persistence.dto.exposed.EntityUnit
import persistence.dto.exposed.Tables
import java.util.*

class ItemIncomeMapper :
    Mapper<Unit, EntityUnit>(toMapper = object : IMapper<ItemIncome, EntityItemIncome> {
        override fun map(input: ItemIncome): EntityItemIncome {
            val entityIncome = EntityItemIncome.apply {

            }

            val entityUnit = EntityUnit(EntityID(UUID.fromString(input.id), Tables.Units))
            entityUnit.apply {
                unit = input.unit
                isMultipliable = input.isMultipliable
            }
            return entityUnit
        }

    }, fromMapper = object : IMapper<EntityItemIncome, ItemIncome> {
        override fun map(input: EntityItemIncome): ItemIncome {

        }

    })
