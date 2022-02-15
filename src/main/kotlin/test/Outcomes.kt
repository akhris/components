package test

import domain.entities.ItemOutcome
import java.time.LocalDateTime
import java.time.Month

object Outcomes {
    val outcome1 = ItemOutcome(
        item = Items.Resistors.resistor1,
        container = Containers.box1,
        quantity = 5L,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 2, 8, 0)
    )
    val outcome2 = ItemOutcome(
        item = Items.Resistors.resistor2,
        container = Containers.box2,
        quantity = 10L,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 5, 15, 30)
    )
}