package strings

import com.akhris.domain.core.utils.log

class StringProvider(private val stringGetter: IStringGetter = ENStringGetter()) {

    fun getLocalizedString(id: String, markNotTranslated: Boolean = true): String {
        val resultString = stringGetter.getString(id)

        return if (resultString != null) {
            resultString
        } else {
            log("localized string for id: $id was not found in translation file for language: ${stringGetter.language}")
            log("trying to get default string for id: $id")
            ENStringGetter().getString(id)?.let { if (markNotTranslated) "!! $it !!" else it }
                ?: throw IllegalStateException("String for id: $id was not found in given string getter: $stringGetter or default getter: $ENStringGetter")
        }
    }

    override fun toString(): String {
        return "string provider for language: ${stringGetter.language}"
    }

    companion object {
        val default = StringProvider()
    }
}