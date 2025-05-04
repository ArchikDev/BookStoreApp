package ru.ar4uk.bookstoreapp.ui.mainScreen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManager
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManager
): ViewModel() {
    val booksListState = mutableStateOf(emptyList<Book>())
    val isFavListEmptyState = mutableStateOf(false)
    val selectedBottomItemState = mutableStateOf(BottomMenuItem.Home.title)
    val categoryState = mutableStateOf("All")
    var bookToDelete: Book? = null

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun getAllBooks() {
        categoryState.value = "All"
        sendUiState(MainUiState.Loading)

        fireStoreManager.getAllBooks(
            onBooks = { books ->
                booksListState.value = books
                isFavListEmptyState.value = books.isEmpty()
                sendUiState(MainUiState.Success)
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }

    fun getAllFavsBooks() {
        categoryState.value = "Favorites"
        sendUiState(MainUiState.Loading)

        fireStoreManager.getAllFavsBooks(
            onBooks = { books ->
                booksListState.value = books
                isFavListEmptyState.value = books.isEmpty()

                sendUiState(MainUiState.Success)
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }

    fun getBooksFromCategory(category: String) {
        if (category == "All") {
            getAllBooks()
            return
        }

        categoryState.value = category

        sendUiState(MainUiState.Loading)

        fireStoreManager.getAllBooksFromCategory(
            category,
            onBooks = { books->
                booksListState.value = books
                isFavListEmptyState.value = books.isEmpty()
                sendUiState(MainUiState.Success)
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }

    fun onFavClick(book: Book, isFavState: String) {
        val booksList = fireStoreManager.changeFavState(booksListState.value, book)
        booksListState.value = if (isFavState == BottomMenuItem.Favs.title) {
            booksList.filter { it.isFavorite }
        } else {
            booksList
        }
        isFavListEmptyState.value = booksListState.value.isEmpty()
    }

    fun deleteBook() {
        if (bookToDelete == null) return

        fireStoreManager.deleteBook(
            bookToDelete!!,
            onDeleted = {
                booksListState.value = booksListState.value.filter {
                    it.id != bookToDelete!!.id
                }
            },
            onFailure = {
                sendUiState(MainUiState.Error(it))
            }
        )
    }

    sealed class MainUiState {
        data object Loading: MainUiState()
        data object Success: MainUiState()
        data class Error(val message: String): MainUiState()
    }
}