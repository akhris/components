package strings

/**
 * Interface for string data source.
 */
interface IStringDataSource {
    val language: String
    fun getString(id: String): String?
}