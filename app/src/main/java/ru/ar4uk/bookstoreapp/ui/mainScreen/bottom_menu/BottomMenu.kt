package ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.ar4uk.bookstoreapp.ui.theme.DarkBlue
import ru.ar4uk.bookstoreapp.ui.theme.PurpleGrey80

@Composable
fun BottomMenu(
    selectedItem: Int,
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
                selected = selectedItem == item.titleId,
                onClick = {
                    when(item.titleId) {
                        BottomMenuItem.Home.titleId -> onHomeClick()
                        BottomMenuItem.Favs.titleId -> onFavsClick()
                        BottomMenuItem.Settings.titleId -> onSettingsClick()
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
                        text = stringResource(item.titleId)
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