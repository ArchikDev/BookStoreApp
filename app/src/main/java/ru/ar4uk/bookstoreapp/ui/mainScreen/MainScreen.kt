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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.custom.MyDialog
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenu
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem
import ru.ar4uk.bookstoreapp.ui.mainScreen.top_app_bar.MainTopBar
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
    onBookClick: (Book) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDeleteDialog = remember { mutableStateOf(false) }

    val isAdminState = remember {
        mutableStateOf(false)
    }

    val state = rememberPullToRefreshState()

    val books = viewModel.books.collectAsLazyPagingItems()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiState.collect { uiState ->
            if (uiState is MainScreenViewModel.MainUiState.Error) {
                Toast.makeText(context, uiState.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(books.loadState.refresh) {
        if (books.loadState.refresh is LoadState.Error) {
            val errorMessage = (books.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMessage,Toast.LENGTH_SHORT).show()
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
//                        viewModel.booksListState.value = emptyList()
                    },
                    onCategoryClick = { categoryIndex ->
                        if (categoryIndex == Categories.FAVORITES) {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Favs.titleId
                        } else {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                        }

                        viewModel.getBooksFromCategory(categoryIndex)
                        books.refresh()

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainTopBar(
                    titleIndex = viewModel.categoryState.intValue
                )
            },
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomMenu(
                viewModel.selectedBottomItemState.intValue,
                onFavsClick = {
                    viewModel.selectedBottomItemState.intValue = BottomMenuItem.Favs.titleId
                    viewModel.getBooksFromCategory(Categories.FAVORITES)

                    books.refresh()
                },
                onHomeClick = {
                    viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                    viewModel.getBooksFromCategory(Categories.ALL)

                    books.refresh()
                },
                onSettingsClick = {
                    viewModel.selectedBottomItemState.intValue = BottomMenuItem.Settings.titleId
                },
            ) }
        ) { paddingValues ->

            if (books.itemCount == 0) {
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
                    viewModel.deleteBook(books.itemSnapshotList.items)
                    showDeleteDialog.value = false
                },
                title = "Attention!",
                message = "Delete this Book?",
                onDismiss = {
                    showDeleteDialog.value = false
                },

            )

//            if (books.loadState.refresh is LoadState.Loading) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(80.dp)
//                    )
//                }
//
//            }

            PullToRefreshBox(
                isRefreshing = books.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    books.refresh()
                },
                state = state,
                modifier = Modifier.padding(paddingValues),
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = books.loadState.refresh is LoadState.Loading,
                        containerColor = Color.Red,
                        color = Color.White,
                        state = state
                    )
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(count = books.itemCount) { index ->

                    }
                }
            }


        }
    }
}


