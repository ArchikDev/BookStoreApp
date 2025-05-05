package ru.ar4uk.bookstoreapp.ui.mainScreen


import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories.ALL
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories.FAVORITES
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManager
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    private val pager: Flow<PagingData<Book>>,
): ViewModel() {
    val booksListState = mutableStateOf(emptyList<Book>())
    val isFavListEmptyState = mutableStateOf(false)
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val categoryState = mutableIntStateOf(ALL)
    var bookToDelete: Book? = null

    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun getBooksFromCategory(categoryIndex: Int) {
        categoryState.intValue = categoryIndex
        fireStoreManager.categoryType = FireStoreManagerPaging.CategoryType.CategoryByIndex(categoryIndex)
    }

    /*
    fun getAllFavsBooks() {
        categoryState.intValue = FAVORITES
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

    fun getBooksFromCategory(categoryIndex: Int) {
        categoryState.intValue = categoryIndex

        sendUiState(MainUiState.Loading)

        fireStoreManager.getAllBooksFromCategory(
            categoryIndex,
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

    fun onFavClick(book: Book, isFavState: Int) {
        val booksList = fireStoreManager.changeFavState(booksListState.value, book)
        booksListState.value = if (isFavState == BottomMenuItem.Favs.titleId) {
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

     */

    sealed class MainUiState {
        data object Loading: MainUiState()
        data object Success: MainUiState()
        data class Error(val message: String): MainUiState()
    }
}