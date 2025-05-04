package ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu

import ru.ar4uk.bookstoreapp.R

sealed class BottomMenuItem(
    val route: String,
    val titleId: Int,
    val iconId: Int
) {
    object Home: BottomMenuItem(
        route = "",
        titleId = R.string.home,
        iconId = R.drawable.ic_home,
    )
    object Favs: BottomMenuItem(
        route = "",
        titleId = R.string.favs,
        iconId = R.drawable.ic_favs,
    )
    object Settings: BottomMenuItem(
        route = "",
        titleId = R.string.settings,
        iconId = R.drawable.ic_settings,
    )
}