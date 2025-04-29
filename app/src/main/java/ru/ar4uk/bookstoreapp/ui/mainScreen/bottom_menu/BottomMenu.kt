package ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun BottomMenu(
    onHomeClick: () -> Unit = {},
    onFavsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Settings
    )

    val seletedItem = remember { mutableStateOf("Home") }

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = seletedItem.value == item.title,
                onClick = {
                    seletedItem.value = item.title

                    when(item.title) {
                        BottomMenuItem.Home.title -> onHomeClick()
                        BottomMenuItem.Favs.title -> onFavsClick()
                        BottomMenuItem.Settings.title -> onSettingsClick()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(item.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = item.title
                    )
                }
            )
        }
    }
}