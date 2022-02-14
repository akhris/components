package ui.screens.types_of_data.types_selector

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed interface ItemRepresentationType : Parcelable {
    @Parcelize
    object Card : ItemRepresentationType

    @Parcelize
    object Table : ItemRepresentationType
}