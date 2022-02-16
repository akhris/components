package test

import domain.entities.EntityCountable
import domain.entities.Project

object Projects {
    val project1 = Project(
        name = "pcb_1",
        description = "commutator pcb with relays",
        items = listOf(
            EntityCountable(entity = Items.Resistors.resistor1, count = 4L),
            EntityCountable(entity = Items.Resistors.resistor2, count = 10L),
            EntityCountable(entity = Items.Capacitors.capacitor1, count = 2L),
        )
    )

    val project2 = Project(
        name = "pcb_2",
        description = "driver test pcb",
        items = listOf(
            EntityCountable(entity = Items.Resistors.resistor1, count = 6L),
            EntityCountable(entity = Items.Capacitors.capacitor2, count = 5L),
        )
    )
}