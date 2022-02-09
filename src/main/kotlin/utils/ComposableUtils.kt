package utils

import androidx.compose.runtime.Composable
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import strings.IStringsProvider
import strings.Strings

@Composable
fun Strings.toLocalizedString(di: DI? = null): String {
    val stringsProvider by (di ?: localDI()).instance<IStringsProvider>()
    return stringsProvider.getString(this)
}