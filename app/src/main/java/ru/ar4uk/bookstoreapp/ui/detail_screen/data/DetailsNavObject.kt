package ru.ar4uk.bookstoreapp.ui.detail_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavObject(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val category: String = ""
)
