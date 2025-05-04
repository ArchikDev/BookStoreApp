package ru.ar4uk.bookstoreapp.ui.detail_screen.data

import kotlinx.serialization.Serializable
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories

@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.FANTASY
)
