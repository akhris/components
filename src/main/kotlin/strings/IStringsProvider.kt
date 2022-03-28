package strings

interface IStringGetter {
    val language: String
    fun getString(id: String): String?
}

typealias LocalizedStrings = (StringsIDs) -> String

val defaultLocalizedStrings: LocalizedStrings = {
    StringProvider.default.getLocalizedString(it.name)
}