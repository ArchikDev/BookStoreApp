package ru.ar4uk.bookstoreapp.utils.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.data.Favorite
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories
import javax.inject.Singleton

@Singleton
class FireStoreManagerPaging(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
) {
    var categoryIndex = Categories.ALL

    suspend fun nexPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection("books").limit(pageSize)

        val keysFavsList = getIdsFavsList()

        query = when(categoryIndex) {
            Categories.ALL -> query
            Categories.FAVORITES -> query.whereIn(FieldPath.documentId(), keysFavsList)
            else -> query.whereEqualTo("category", categoryIndex)
        }

        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }

        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)
        val updatedBooks = books.map {
            if (keysFavsList.contains(it.id)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }

        return Pair(querySnapshot, updatedBooks)
    }

    private suspend fun getIdsFavsList(): List<String> {
        val snapshot = getFavsCategoryReference().get().await()

        val idsList = snapshot.toObjects(Favorite::class.java)
        val keysList = arrayListOf<String>()

        idsList.forEach {
            keysList.add(it.id)
        }

        return if (keysList.isEmpty()) listOf("-1") else keysList
    }

    private fun getFavsCategoryReference(): CollectionReference {
        return db.collection("users")
            .document(auth.uid ?: "")
            .collection("favorites")
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

    private fun saveBookToFireStore(
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val db = db.collection("books")
        val id = book.id.ifEmpty { db.document().id }

        db.document(id)
            .set(
                book.copy(id = id)
            )
            .addOnSuccessListener {
                onSaved()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error")
            }
    }

    fun saveBookImage(
        oldImageUrl: String,
        uri: Uri?,
        book: Book,
        onSaved: () -> Unit,
        onError: (String) -> Unit
    ) {
        val timeStamp = System.currentTimeMillis()

        val storageRef = if (oldImageUrl.isEmpty()) {
            storage.reference
                .child("book_images")
                .child("image_$timeStamp.jpg")
        } else {
            storage.getReferenceFromUrl(oldImageUrl)
        }
        if (uri == null) {
            saveBookToFireStore(
                book = book.copy(imageUrl = oldImageUrl),
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError(it)
                }
            )

            return
        }

        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { url ->
                saveBookToFireStore(
                    book = book.copy(imageUrl = url.toString()),
                    onSaved = {
                        onSaved()
                    },
                    onError = {
                        onError(it)
                    }
                )
            }
        }

    }
}