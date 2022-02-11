package test

import domain.entities.Supplier

object Suppliers {
    val chipdip = Supplier(
        name = "Чип и Дип",
        url = "http://www.chipdip.ru",
        isFavorite = true
    )

    val platan = Supplier(
        name = "Платан",
        url = "http://www.platan.ru",
        isFavorite = false
    )

    val dkoElectronshik = Supplier(
        name = "ДКО Электронщик",
        url = "http://www.electronshik.ru",
        isFavorite = true
    )

    val quartz = Supplier(
        name = "Кварц",
        url = "http://www.quartz1.com",
        isFavorite = false
    )

    fun getAll() = listOf(chipdip, platan, dkoElectronshik, quartz)

}