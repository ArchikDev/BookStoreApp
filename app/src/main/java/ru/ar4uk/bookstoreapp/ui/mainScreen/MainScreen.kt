package ru.ar4uk.bookstoreapp.ui.mainScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.custom.MyDialog
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.data.Favorite
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenu
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
    onBookClick: (Book) -> Unit
) {
    val showLoadIndicator = remember {
        mutableStateOf(true)
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }

    val isAdminState = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (viewModel.booksListState.value.isEmpty()) {
            viewModel.getAllBooks()
        }

        viewModel.uiState.collect { state ->
            when(state) {
                is MainScreenViewModel.MainUiState.Loading -> {
                    showLoadIndicator.value = true
                }
                is MainScreenViewModel.MainUiState.Success -> {
                    showLoadIndicator.value = false
                }
                is MainScreenViewModel.MainUiState.Error -> {
                    showLoadIndicator.value = false

                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                    },
                    onFavsClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Favs.title

                        viewModel.getAllFavsBooks()

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    onCategoryClick = { category ->
                        viewModel.getBooksFromCategory(category)
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Home.title

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomMenu(
                viewModel.selectedBottomItemState.value,
                onFavsClick = {
                    viewModel.selectedBottomItemState.value = BottomMenuItem.Favs.title

                    viewModel.getAllFavsBooks()
                },
                onHomeClick = {
                    viewModel.selectedBottomItemState.value = BottomMenuItem.Home.title

                    viewModel.getAllBooks()
                },
                onSettingsClick = {
                    viewModel.selectedBottomItemState.value = BottomMenuItem.Settings.title
                },
            ) }
        ) { paddingValues ->

            if (viewModel.isFavListEmptyState.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Empty list",
                        color = Color.LightGray
                    )
                }
            }

            MyDialog(
                showDialog = showDeleteDialog.value,
                onConfirm = {
                    viewModel.deleteBook()
                    showDeleteDialog.value = false
                },
                title = "Attention!",
                message = "Delete this Book?",
                onDismiss = {
                    showDeleteDialog.value = false
                },

            )

            if (showLoadIndicator.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp)
                    )
                }

            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(viewModel.booksListState.value) { book ->
                    BookListItemUi(
                        isAdminState.value,
                        book,
                        onEditClick = { bk ->
                            onBookEditClick(bk)
                        },
                        onFavoriteClick = {
                            viewModel.onFavClick(
                                book,
                                viewModel.selectedBottomItemState.value
                            )
                        },
                        onBookClick = { bk ->
                            onBookClick(bk)
                        },
                        onDeleteClick = { bkToDelete ->
                            showDeleteDialog.value = true
                            viewModel.bookToDelete = bkToDelete
                        }
                    )
                }
            }
        }
    }
}


