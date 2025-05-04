package ru.ar4uk.bookstoreapp.ui.add_book_screen.data

import kotlinx.serialization.Serializable
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories

@Serializable
data class AddScreenObject(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val categoryIndex: Int = Categories.FANTASY,
    val imageUrl: String = ""
)