package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.Value

object Values {
    object Resistances{
        val value1_1 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.resistance, value = "100")
        val value1_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.wattage, value = "2")
        val value1_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "0,1")

        val value2_1 =
            Value(id = IDUtils.newID(), parameter = Parameters.Electronic.resistance, value = "150", factor = 1e3f)
        val value2_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.wattage, value = "1")
        val value2_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "5")
    }

    object Capacities {
        val value1_1 =
            Value(id = IDUtils.newID(), parameter = Parameters.Electronic.capacitance, value = "100", factor = 1e-9f)
        val value1_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.maxVoltage, value = "50")
        val value1_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "20")
        val value1_4 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.dielectricType, value = "NP0")

        val value2_1 =
            Value(id = IDUtils.newID(), parameter = Parameters.Electronic.resistance, value = "1", factor = 1e-6f)
        val value2_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.maxVoltage, value = "25")
        val value2_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "10")
        val value2_4 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.dielectricType, value = "X5R")


    }
}