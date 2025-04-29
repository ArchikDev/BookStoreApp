package ru.ar4uk.bookstoreapp.ui.mainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.data.Favorite
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.bottom_menu.BottomMenu

@Composable
fun MainScreen(
    navData: MainScreenDataObject,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val booksListState = remember {
        mutableStateOf(emptyList<Book>())
    }
    val isAdminState = remember {
        mutableStateOf(false)
    }

    val db = remember { Firebase.firestore }

    LaunchedEffect(Unit) {
        getAllFavsIds(db, navData.uid) { favs ->
            getAllBooks(db, favs) { books ->
                booksListState.value = books
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
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllFavsBooks(db, favs) { books ->
                                booksListState.value = books
                            }
                        }
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
                onFavsClick = {
                    getAllFavsIds(db, navData.uid) { favs ->
                        getAllFavsBooks(db, favs) { books ->
                            booksListState.value = books
                        }
                    }
                },
                onHomeClick = {
                    getAllFavsIds(db, navData.uid) { favs ->
                        getAllBooks(db, favs) { books ->
                            booksListState.value = books
                        }
                    }
                },
            ) }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(booksListState.value) { book ->
                    BookListItemUi(
                        isAdminState.value,
                        book,
                        onEditClick = { book ->
                            onBookEditClick(book)
                        },
                        onFavoriteClick = {
                            booksListState.value = booksListState.value.map {
                                if (it.id == book.id) {
                                    onFavs(
                                        db,
                                        navData.uid,
                                        Favorite(it.id),
                                        !it.isFavorite
                                    )
                                    it.copy(isFavorite = !it.isFavorite)
                                } else {
                                    it
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


private fun getAllBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>) -> Unit
) {
    db.collection("books")
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.id)) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }

            onBooks(booksList)
        }
        .addOnFailureListener {  }
}

private fun getAllFavsBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>) -> Unit
) {
    db.collection("books")
        .whereIn(FieldPath.documentId(), idsList)
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.id)) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }

            onBooks(booksList)
        }
        .addOnFailureListener {  }
}

private fun getAllFavsIds(
    db: FirebaseFirestore,
    uid: String,
    onFavs: (List<String>) -> Unit
) {
    db.collection("users")
        .document(uid)
        .collection("favorites")
        .get()
        .addOnSuccessListener { task ->
            val idsList = task.toObjects(Favorite::class.java)
            val keysList = arrayListOf<String>()
            idsList.forEach {
                keysList.add(it.id)
            }
            onFavs(keysList)
        }
        .addOnFailureListener {  }
}

private fun onFavs(
    db: FirebaseFirestore,
    uid: String,
    favorite: Favorite,
    isFav: Boolean
) {
    if (isFav) {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.id)
            .set(favorite)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    } else {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .document(favorite.id)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

}