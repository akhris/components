package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Value


object ResistorTestEntities {

    val resistorsType = ObjectType(
        id = IDUtils.newID(),
        name = "resistors",
        parameters = listOf(
            Parameters.Electronic.resistance,
            Parameters.Electronic.wattage,
            Parameters.Electronic.tolerance,
            Parameters.Electronic.packg
        )
    )


    object Values {
        val value1_1 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.resistance, value = "100")
        val value1_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.wattage, value = "2")
        val value1_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "0,1")

        val value2_1 =
            Value(id = IDUtils.newID(), parameter = Parameters.Electronic.resistance, value = "150", factor = 1e3f)
        val value2_2 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.wattage, value = "1")
        val value2_3 = Value(id = IDUtils.newID(), parameter = Parameters.Electronic.tolerance, value = "5")

    }

    object Items {
        val resistor1 = Item(
            id = IDUtils.newID(),
            name = "resistor 1",
            type = resistorsType,
            values = listOf(
                ResistorTestEntities.Values.value1_1,
                ResistorTestEntities.Values.value1_2,
                ResistorTestEntities.Values.value1_3
            )
        )
        val resistor2 = Item(
            id = IDUtils.newID(),
            name = "resistor 2",
            type = resistorsType,
            values = listOf(
                ResistorTestEntities.Values.value2_1,
                ResistorTestEntities.Values.value2_2,
                ResistorTestEntities.Values.value2_3
            )
        )
    }
}


object CapacitorTestEntities {

    val capacitorsType = ObjectType(
        id = IDUtils.newID(),
        name = "capacitors",
        parameters = listOf(
            Parameters.Electronic.capacitance,
            Parameters.Electronic.maxVoltage,
            Parameters.Electronic.tolerance,
            Parameters.Electronic.dielectricType,
            Parameters.Electronic.packg
        )
    )


    object Values {
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

    object Items {
        val capacitor1 = Item(
            id = IDUtils.newID(),
            name = "resistor 1",
            type = capacitorsType,
            values = listOf(Values.value1_1, Values.value1_2, Values.value1_3, Values.value1_4)
        )
        val capacitor2 = Item(
            id = IDUtils.newID(),
            name = "resistor 2",
            type = capacitorsType,
            values = listOf(
                Values.value2_1,
                Values.value2_2,
                Values.value2_3,
                Values.value2_4
            )
        )
    }
}