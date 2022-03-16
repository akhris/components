package ui.screens.entities_screen.entities_view_settings

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed interface ItemRepresentationType : Parcelable {
    @Parcelize
    object Card : ItemRepresentationType

    @Parcelize
    object Table : ItemRepresentationType
}