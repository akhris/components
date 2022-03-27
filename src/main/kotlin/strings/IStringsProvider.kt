package strings

interface IStringsProvider {
    fun getString(strings: Strings): String
}

interface IStringGetter {
    val language: String
    fun getString(id: String): String?
}