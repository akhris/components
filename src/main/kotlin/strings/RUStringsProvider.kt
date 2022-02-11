package strings

class RUStringsProvider : IStringsProvider {
    override fun getString(strings: Strings): String {
        return when (strings) {
            Strings.navitem_database_title -> "источник данных"
            Strings.navitem_warehouse_title -> "склад"
            Strings.navitem_datatypes_title -> "типы данных"
            Strings.navitem_income_title -> "пополнение"
            Strings.navitem_outcome_title -> "изъятие"
            Strings.navitem_projects_title -> "проекты"
            Strings.navitem_settings_title -> "настройки"
            Strings.navitem_places_title -> "места"
            Strings.TypesOfData.items_description -> "Компоненты, которые хранятся на складе (без привязки к количеству)"
            Strings.TypesOfData.items_title -> "компоненты"
            Strings.TypesOfData.parameters_description -> "Параметры, которые характеризуют компоненты (сопротивление, ёмкость, размер, ...)"
            Strings.TypesOfData.parameters_title -> "параметры"
            Strings.TypesOfData.types_description -> "Типы компонентов (резисторы, конденсаторы, ...) и их параметры."
            Strings.TypesOfData.types_title -> "типы компонентов"
            Strings.TypesOfData.units_description -> "Единицы измерения, в которых измеряются значения параметров (Ом, м, А, ...)"
            Strings.TypesOfData.units_title -> "единицы измерения"
            Strings.TypesOfData.containers_description -> "Ящики, шкафы, комнаты - все места, где хранятся компоненты"
            Strings.TypesOfData.containers_title -> "контейнеры"
            Strings.TypesOfData.suppliers_description -> "Магазины, физ.лица и прочие места, где можно получить компоненты"
            Strings.TypesOfData.suppliers_title -> "поставщики"
        }
    }
}