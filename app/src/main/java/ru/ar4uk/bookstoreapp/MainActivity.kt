package ru.ar4uk.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import ru.ar4uk.bookstoreapp.ui.add_book_screen.AddBookScreen
import ru.ar4uk.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import ru.ar4uk.bookstoreapp.ui.detail_screen.data.DetailsNavObject
import ru.ar4uk.bookstoreapp.ui.detail_screen.ui.DetailsScreen
import ru.ar4uk.bookstoreapp.ui.login.LoginScreen
import ru.ar4uk.bookstoreapp.ui.login.data.LoginScreenObject
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.MainScreen

@AndroidEntryPoint
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
                        navData = navData,
                        onBookEditClick = { book ->
                            navController.navigate(AddScreenObject(
                                id = book.id,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                categoryIndex = book.category,
                                imageUrl = book.imageUrl
                            ))
                        },
                        onBookClick = { bk ->
                            navController.navigate(DetailsNavObject(
                                title = bk.title,
                                description = bk.description,
                                imageUrl = bk.imageUrl,
                                price = bk.price,
                                categoryIndex = bk.category,
                            ))
                        },
                        onAdminClick = {
                            navController.navigate(AddScreenObject())
                        }
                    )
                }

                composable<AddScreenObject> { navEntry->
                    val navData = navEntry.toRoute<AddScreenObject>()

                    AddBookScreen(
                        navData,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<DetailsNavObject> { navEntry->
                    val navData = navEntry.toRoute<DetailsNavObject>()

                    DetailsScreen(
                        navData
                    )
                }
            }
        }
    }


}