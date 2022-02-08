package test

import com.akhris.domain.core.utils.IDUtils
import domain.entities.ObjectType

object Types {
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
}