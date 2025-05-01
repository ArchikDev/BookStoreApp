package ru.ar4uk.bookstoreapp.ui.mainScreen


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenuItem
import ru.ar4uk.bookstoreapp.utils.firebase.FirebaseManager
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val firebaseManager: FirebaseManager
): ViewModel() {
    val booksListState = mutableStateOf(emptyList<Book>())
    val isFavListEmptyState = mutableStateOf(false)
    val selectedBottomItemState = mutableStateOf(BottomMenuItem.Home.title)

    fun getAllBooks() {
        firebaseManager.getAllBooks { books ->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
    }

    fun getAllFavsBooks() {
        firebaseManager.getAllFavsBooks { books ->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
    }

    fun getBooksFromCategory(category: String) {
        if (category == "All") {
            getAllBooks()
            return
        }

        firebaseManager.getAllBooksFromCategory(category) { books->
            booksListState.value = books
            isFavListEmptyState.value = books.isEmpty()
        }
    }

    fun onFavClick(book: Book, isFavState: String) {
        val booksList = firebaseManager.changeFavState(booksListState.value, book)
        booksListState.value = if (isFavState == BottomMenuItem.Favs.title) {
            booksList.filter { it.isFavorite }
        } else {
            booksList
        }
        isFavListEmptyState.value = booksListState.value.isEmpty()
    }
}