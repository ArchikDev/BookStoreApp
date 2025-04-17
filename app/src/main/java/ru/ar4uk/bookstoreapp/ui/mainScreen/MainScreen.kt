package ru.ar4uk.bookstoreapp.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(DrawerValue.Open)

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(.7f),
        drawerContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                DrawerHeader()
                DrawerBody()
            }
        }
    ) {

    }
}