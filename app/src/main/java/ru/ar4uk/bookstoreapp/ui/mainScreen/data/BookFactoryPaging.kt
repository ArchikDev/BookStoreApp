package ru.ar4uk.bookstoreapp.ui.mainScreen.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import okio.IOException
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManagerPaging
import javax.inject.Inject

class BookFactoryPaging @Inject constructor(
    private val firestorePagingManager: FireStoreManagerPaging
): PagingSource<DocumentSnapshot, Book>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Book>): DocumentSnapshot? {
        return null
    }
    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Book> {
        try {
            val currentPage = params.key

            val (snapshot, books) = firestorePagingManager.nexPage(
                pageSize = params.loadSize.toLong(),
                currentKey = currentPage
            )

            val prevKey = if (currentPage == null) null else snapshot.documents.firstOrNull()
            val nextKey = snapshot.documents.lastOrNull()

            return LoadResult.Page(
                data = books,
                prevKey = prevKey,
                nextKey = nextKey,
            )

        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}