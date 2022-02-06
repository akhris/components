package test

import com.akhris.domain.core.utils.IDUtils

object Units {
    object Electronic {
        val ohm = domain.entities.Unit(id = IDUtils.newID(), "ohm", isMultipliable = true)
        val watt = domain.entities.Unit(id = IDUtils.newID(), "watt", isMultipliable = true)
        val amps = domain.entities.Unit(id = IDUtils.newID(), "A", isMultipliable = true)
        val volts = domain.entities.Unit(id = IDUtils.newID(), "V", isMultipliable = true)
        val farads = domain.entities.Unit(id = IDUtils.newID(), "F", isMultipliable = true)
    }

    object Common {
        val percent = domain.entities.Unit(id = IDUtils.newID(), "%", isMultipliable = false)
        val pcs = domain.entities.Unit(id = IDUtils.newID(), "pcs", isMultipliable = false)
        val meters = domain.entities.Unit(id = IDUtils.newID(), "m", isMultipliable = true)
        val grams = domain.entities.Unit(id = IDUtils.newID(), "g", isMultipliable = true)
    }
}