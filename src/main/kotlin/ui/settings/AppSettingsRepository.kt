package ui.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppSettingsRepository(val scope: CoroutineScope) {
    private val _isLightTheme = MutableStateFlow<Boolean>(true)

    val isLightTheme: StateFlow<Boolean> = _isLightTheme

    fun setIsLightTheme(isLight: Boolean) {
        scope.launch {
            _isLightTheme.emit(isLight)
        }
    }

    init {
        setIsLightTheme(true)
    }

}
