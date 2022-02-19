package test

import domain.entities.EntityCountable
import domain.entities.ItemOutcome
import java.time.LocalDateTime
import java.time.Month

object Outcomes {
    val outcome1 = ItemOutcome(
        item = EntityCountable(Items.Resistors.resistor1, 5L),
        container = Containers.box1,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 2, 8, 0)
    )
    val outcome2 = ItemOutcome(
        item = EntityCountable(Items.Resistors.resistor2, 10L),
        container = Containers.box2,
//        quantity = 10L,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 5, 15, 30)
    )
}