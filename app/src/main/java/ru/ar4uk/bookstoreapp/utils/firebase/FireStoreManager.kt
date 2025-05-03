package ru.ar4uk.bookstoreapp.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.data.Favorite
import javax.inject.Singleton

@Singleton
class FireStoreManager(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private fun getAllFavsIds(
        onFavs: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getFavsCategoryReference()
            .get()
            .addOnSuccessListener { task ->
                val idsList = task.toObjects(Favorite::class.java)
                val keysList = arrayListOf<String>()
                idsList.forEach {
                    keysList.add(it.id)
                }
                onFavs(keysList)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }

    fun getAllFavsBooks(
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getAllFavsIds(
            onFavs = { idsList->
                if (idsList.isNotEmpty()) {
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
                        .addOnFailureListener {
                            onFailure(it.message ?: "Error")
                        }
                } else {
                    onBooks(emptyList())
                }
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun getAllBooks(
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getAllFavsIds(
            onFavs = { idsList->
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
                    .addOnFailureListener {
                        onFailure(it.message ?: "Error")
                    }
            },
            onFailure = {
                onFailure(it)
            }
        )
    }

    fun getAllBooksFromCategory(
        category: String,
        onBooks: (List<Book>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        getAllFavsIds(
            onFavs = { idsList->
                db.collection("books")
                    .whereEqualTo("category", category)
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
                    .addOnFailureListener {
                        onFailure(it.message ?: "Error")
                    }
            },
            onFailure = {
                onFailure(it)
            }
        )

    }

    fun onFavs(
        favorite: Favorite,
        isFav: Boolean
    ) {
        val favsDocRef = getFavsCategoryReference()
            .document(favorite.id)

        if (isFav) {
            favsDocRef
                .set(favorite)
                .addOnSuccessListener { }
                .addOnFailureListener {
                }
        } else {
            favsDocRef
                .delete()
                .addOnSuccessListener { }
                .addOnFailureListener {
                }
        }

    }

    fun changeFavState(books: List<Book>, book: Book): List<Book> {
        return books.map {
            if (it.id == book.id) {
                onFavs(
                    Favorite(it.id),
                    !it.isFavorite
                )
                it.copy(isFavorite = !it.isFavorite)
            } else {
                it
            }
        }
    }

    private fun getFavsCategoryReference(): CollectionReference {
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favorites")
    }

    fun deleteBook(
        book: Book,
        onDeleted: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("books")
            .document(book.id)
            .delete()
            .addOnSuccessListener {
                onDeleted()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Error")
            }
    }
}