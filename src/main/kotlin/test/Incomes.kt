package test

import domain.entities.ItemIncome
import java.time.LocalDateTime
import java.time.Month

object Incomes {
    val income1 = ItemIncome(
        item = Items.Resistors.resistor1,
        container = Containers.box1,
        quantity = 10L,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 1, 12, 0),
        supplier = Suppliers.chipdip
    )
    val income2 = ItemIncome(
        item = Items.Resistors.resistor2,
        container = Containers.box2,
        quantity = 40L,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 2, 10, 0),
        supplier = Suppliers.dkoElectronshik
    )
}