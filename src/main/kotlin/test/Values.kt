package test

import domain.entities.EntityValuable

object Values {
    object Resistances {
        val value1_1 = EntityValuable(entity = Parameters.Electronic.resistance, value = "100")
        val value1_2 = EntityValuable(entity = Parameters.Electronic.wattage, value = "2")
        val value1_3 = EntityValuable(entity = Parameters.Electronic.tolerance, value = "0,1")

        val value2_1 = EntityValuable(entity = Parameters.Electronic.resistance, value = "150", factor = 3)
        val value2_2 = EntityValuable(entity = Parameters.Electronic.wattage, value = "1")
        val value2_3 = EntityValuable(entity = Parameters.Electronic.tolerance, value = "5")
    }

    object Capacities {
        val value1_1 = EntityValuable(entity = Parameters.Electronic.capacitance, value = "100", factor = -9)
        val value1_2 = EntityValuable(entity = Parameters.Electronic.maxVoltage, value = "50")
        val value1_3 = EntityValuable(entity = Parameters.Electronic.tolerance, value = "20")
        val value1_4 = EntityValuable(entity = Parameters.Electronic.dielectricType, value = "NP0")

        val value2_1 = EntityValuable(entity = Parameters.Electronic.resistance, value = "1", factor = -6)
        val value2_2 = EntityValuable(entity = Parameters.Electronic.maxVoltage, value = "25")
        val value2_3 = EntityValuable(entity = Parameters.Electronic.tolerance, value = "10")
        val value2_4 = EntityValuable(entity = Parameters.Electronic.dielectricType, value = "X5R")
    }
}