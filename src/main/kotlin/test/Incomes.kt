package test

import domain.entities.EntityCountable
import domain.entities.ItemIncome
import java.time.LocalDateTime
import java.time.Month

object Incomes {
    val income1 = ItemIncome(
        item = EntityCountable( Items.Resistors.resistor1, 10L),
        container = Containers.box1,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 1, 12, 0),
        supplier = Suppliers.chipdip
    )
    val income2 = ItemIncome(
        item = EntityCountable(Items.Resistors.resistor2, 40L),
        container = Containers.box2,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 2, 10, 0),
        supplier = Suppliers.dkoElectronshik
    )
    val income3 = ItemIncome(
        item = EntityCountable(Items.Resistors.resistor2, 40L),
        container = Containers.box3,
        dateTime = LocalDateTime.of(2020, Month.APRIL, 3, 10, 0),
        supplier = Suppliers.dkoElectronshik
    )
}