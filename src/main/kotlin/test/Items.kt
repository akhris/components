package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.Item

object Items {
    object Resistors {
        val resistor1 = Item(
            id = IDUtils.newID(),
            name = "resistor 1",
            type = Types.resistorsType,
            values = listOf(
                Values.Resistances.value1_1,
                Values.Resistances.value1_2,
                Values.Resistances.value1_3
            )
        )
        val resistor2 = Item(
            id = IDUtils.newID(),
            name = "resistor 2",
            type = Types.resistorsType,
            values = listOf(
                Values.Resistances.value2_1,
                Values.Resistances.value2_2,
                Values.Resistances.value2_3
            )
        )
    }

    object Capacitors {
        val capacitor1 = Item(
            id = IDUtils.newID(),
            name = "capacitor 1",
            type = Types.capacitorsType,
            values = listOf(
                Values.Capacities.value1_1,
                Values.Capacities.value1_2,
                Values.Capacities.value1_3,
                Values.Capacities.value1_4
            )
        )
        val capacitor2 = Item(
            id = IDUtils.newID(),
            name = "capacitor 2",
            type = Types.capacitorsType,
            values = listOf(
                Values.Capacities.value2_1,
                Values.Capacities.value2_2,
                Values.Capacities.value2_3,
                Values.Capacities.value2_4
            )
        )
    }
}