package persistence.mappers

import com.akhris.domain.core.mappers.IMapper
import com.akhris.domain.core.mappers.Mapper
import domain.entities.Unit
import org.jetbrains.exposed.dao.id.EntityID
import persistence.dto.exposed.EntityUnit
import persistence.dto.exposed.Tables
import java.util.*

class UnitMapper :
    Mapper<Unit, EntityUnit>(toMapper = object : IMapper<Unit, EntityUnit> {
        override fun map(input: Unit): EntityUnit {
            val entityUnit = EntityUnit

                entityUnit.apply {
                unit = input.unit
                isMultipliable = input.isMultipliable
            }
            return entityUnit
        }

    }, fromMapper = object : IMapper<EntityUnit, Unit> {
        override fun map(input: EntityUnit): Unit {
            return Unit(id = input.id.value.toString(), unit = input.unit, isMultipliable = input.isMultipliable)
        }

    })