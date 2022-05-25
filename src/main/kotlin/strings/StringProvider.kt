package strings

import com.akhris.domain.core.utils.log

class StringProvider(private val stringDataSource: IStringDataSource = ENStringDataSource()) {

    fun getLocalizedString(id: String, markNotTranslated: Boolean = true): String {
        val resultString = stringDataSource.getString(id)

        return if (resultString != null) {
            resultString
        } else {
            log("localized string for id: $id was not found in translation file for language: ${stringDataSource.language}")
            log("trying to get default string for id: $id")
            ENStringDataSource().getString(id)?.let { if (markNotTranslated) "!! $it !!" else it }
                ?: throw IllegalStateException("String for id: $id was not found in given string getter: $stringDataSource or default getter: $ENStringDataSource")
        }
    }

    override fun toString(): String {
        return "string provider for language: ${stringDataSource.language}"
    }

    companion object {
        val default = StringProvider()
    }
}