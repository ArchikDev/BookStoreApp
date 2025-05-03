package ru.ar4uk.bookstoreapp.ui.mainScreen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun getAllBooks() {
        fireStoreManager.getAllBooks { books ->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
    }

    fun getAllFavsBooks() {
        fireStoreManager.getAllFavsBooks { books ->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
    }

    fun getBooksFromCategory(category: String) {
        if (category == "All") {
            getAllBooks()
            return
        }

        fireStoreManager.getAllBooksFromCategory(category) { books->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
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

    fun deleteBook(
        book: Book
    ) {
        fireStoreManager.deleteBook(
            book,
            onDeleted = {
                booksListState.value = booksListState.value.filter {
                    it.id != book.id
                }
            }
        )
    }
}