package ru.ar4uk.bookstoreapp.data

import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories

data class Book(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: Int = Categories.FANTASY,
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)
