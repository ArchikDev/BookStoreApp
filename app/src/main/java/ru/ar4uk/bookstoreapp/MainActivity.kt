package ru.ar4uk.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.ar4uk.bookstoreapp.ui.add_book_screen.AddBookScreen
import ru.ar4uk.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import ru.ar4uk.bookstoreapp.ui.login.LoginScreen
import ru.ar4uk.bookstoreapp.ui.login.data.LoginScreenObject
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ) {
                composable<LoginScreenObject> {
                    LoginScreen(
                        onNavigateToMainScreen = { navData ->
                            navController.navigate(navData)
                        }
                    )
                }
                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()

                    MainScreen(
                        navData,
                        onBookEditClick = { book ->
                            navController.navigate(AddScreenObject(
                                id = book.id,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                category = book.category,
                                imageUrl = book.imageUrl
                            ))
                        }
                    ) {
                        navController.navigate(AddScreenObject)
                    }
                }

                composable<AddScreenObject> { navEntry->
                    val navData = navEntry.toRoute<AddScreenObject>()

                    AddBookScreen(navData)
                }
            }
        }
    }


}