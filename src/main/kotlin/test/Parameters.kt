package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.Parameter

object Parameters {
    object Electronic {
        val resistance = Parameter(
            id = IDUtils.newID(),
            name = "resistance",
            description = "resistance value",
            unit = Units.Electronic.ohm
        )
        val wattage = Parameter(
            id = IDUtils.newID(),
            name = "power rating",
            description = "maximum emitted power",
            unit = Units.Electronic.watt
        )
        val tolerance = Parameter(
            id = IDUtils.newID(),
            name = "tolerance",
            description = "maximum value tolerance",
            unit = Units.Common.percent
        )
        val capacitance = Parameter(
            id = IDUtils.newID(),
            name = "capacitance",
            description = "resistance value",
            unit = Units.Electronic.farads
        )
        val maxVoltage = Parameter(
            id = IDUtils.newID(),
            name = "maximum voltage",
            description = "maximum applied voltage",
            unit = Units.Electronic.volts
        )
        val dielectricType =
            Parameter(id = IDUtils.newID(), name = "dielectric type", description = "dielectric material type")
        val packg = Parameter(id = IDUtils.newID(), name = "package", description = "string representing package size")
    }

    object Material {
        val length = Parameter(id = IDUtils.newID(), name = "length", unit = Units.Common.meters)
        val weight = Parameter(id = IDUtils.newID(), name = "weight", unit = Units.Common.grams)
    }
}