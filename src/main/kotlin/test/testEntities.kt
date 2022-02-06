package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.Item
import domain.entities.ObjectType
import domain.entities.Parameter
import domain.entities.Value
import test.ResistorTestEntities.Parameters.param1
import test.ResistorTestEntities.Parameters.param2
import test.ResistorTestEntities.Parameters.param3
import test.ResistorTestEntities.Units.ohm
import test.ResistorTestEntities.Units.percent
import test.ResistorTestEntities.Units.watt
import test.ResistorTestEntities.Values.value1_1
import test.ResistorTestEntities.Values.value1_2
import test.ResistorTestEntities.Values.value1_3
import test.ResistorTestEntities.Values.value2_1
import test.ResistorTestEntities.Values.value2_2
import test.ResistorTestEntities.Values.value2_3

object ResistorTestEntities {

    val resistorsType = ObjectType(
        id = IDUtils.newID(),
        name = "resistors",
        parameters = listOf(param1, param2, param3)
    )

    object Units {
        val ohm = domain.entities.Unit(id = IDUtils.newID(), "ohm", factor = 1f)
        val kohm = domain.entities.Unit(id = IDUtils.newID(), "kohm", factor = 1e3f)
        val watt = domain.entities.Unit(id = IDUtils.newID(), "watt")
        val percent = domain.entities.Unit(id = IDUtils.newID(), "%")
    }

    object Parameters {
        val param1 = Parameter(id = IDUtils.newID(), name = "resistance", unit = ohm)   //fixme what to do with kohm?
        val param2 = Parameter(id = IDUtils.newID(), name = "power rating", unit = watt)
        val param3 = Parameter(id = IDUtils.newID(), name = "tolerance", unit = percent)
    }


    object Values {
        val value1_1 = Value(id = IDUtils.newID(), parameter = param1, value = "100")
        val value1_2 = Value(id = IDUtils.newID(), parameter = param2, value = "2")
        val value1_3 = Value(id = IDUtils.newID(), parameter = param3, value = "0,1")

        val value2_1 = Value(id = IDUtils.newID(), parameter = param1, value = "150")
        val value2_2 = Value(id = IDUtils.newID(), parameter = param2, value = "1")
        val value2_3 = Value(id = IDUtils.newID(), parameter = param3, value = "5")

    }

    object Items {
        val item1 = Item(
            id = IDUtils.newID(),
            name = "resistor 1",
            type = resistorsType,
            values = listOf(value1_1, value1_2, value1_3)
        )
        val item2 = Item(
            id = IDUtils.newID(),
            name = "resistor 2",
            type = resistorsType,
            values = listOf(value2_1, value2_2, value2_3)
        )
    }

}