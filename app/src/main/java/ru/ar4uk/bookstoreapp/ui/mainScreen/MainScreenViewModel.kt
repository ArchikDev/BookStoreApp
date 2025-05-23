package ru.ar4uk.bookstoreapp.ui.mainScreen


import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories.ALL
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManagerPaging
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManagerPaging,
    private val pager: Flow<PagingData<Book>>,
): ViewModel() {
    val selectedBottomItemState = mutableIntStateOf(BottomMenuItem.Home.titleId)
    val categoryState = mutableIntStateOf(ALL)
    var bookToDelete: Book? = null
    var deleteBook = false

    val booksListUpdate = MutableStateFlow<List<Book>>(emptyList())
    val books: Flow<PagingData<Book>> = pager.cachedIn(viewModelScope)
        .combine(booksListUpdate) { pagingData, booksList ->
            val pgData = pagingData.map { book ->
                val updateBook = booksList.find {
                    it.id == book.id
                }
                updateBook ?: book
            }

            if (deleteBook) {
                deleteBook = false
                pgData.filter { pgBook ->
                    booksList.find {
                        it.id == pgBook.id
                    } != null
                }
            } else {
                pgData
            }
        }


    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun getBooksFromCategory(categoryIndex: Int) {
        categoryState.intValue = categoryIndex
        fireStoreManager.categoryIndex = categoryIndex
    }

    fun onFavClick(book: Book, isFavState: Int, bookList: List<Book>) {
        val booksList = fireStoreManager.changeFavState(bookList, book)
        booksListUpdate.value = if (isFavState == BottomMenuItem.Favs.titleId) {
            deleteBook = true
            booksList.filter { it.isFavorite }
        } else {
            booksList
        }
    }

    fun deleteBook(uiList: List<Book>) {
        if (bookToDelete == null) return

        fireStoreManager.deleteBook(
            bookToDelete!!,
            onDeleted = {
                deleteBook = true
                booksListUpdate.value = uiList.filter {
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