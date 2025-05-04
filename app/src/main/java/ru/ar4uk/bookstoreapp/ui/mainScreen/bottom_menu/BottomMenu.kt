package ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import ru.ar4uk.bookstoreapp.ui.theme.DarkBlue
import ru.ar4uk.bookstoreapp.ui.theme.PurpleGrey80

@Composable
fun BottomMenu(
    selectedItem: String,
    onHomeClick: () -> Unit = {},
    onFavsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val items = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favs,
        BottomMenuItem.Settings
    )

    NavigationBar(
        containerColor = PurpleGrey80
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item.title,
                onClick = {
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
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}