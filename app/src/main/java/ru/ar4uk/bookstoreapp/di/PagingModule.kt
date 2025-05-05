package ru.ar4uk.bookstoreapp.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.mainScreen.data.BookFactoryPaging
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManagerPaging

@Module
@InstallIn(ViewModelComponent::class) // привязан только к ЖЦ viewmodel
object PagingModule {

    @Provides
    @ViewModelScoped
    fun providesPagingFlow(
        fireStoreManagerPaging: FireStoreManagerPaging
    ): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 3, // сколько эл-ов остаётся чтобы пошёл новый запрос
                initialLoadSize = 15 // первый запрос(первая страница), сколько элементов запросить
            ),
            pagingSourceFactory = {
                BookFactoryPaging(fireStoreManagerPaging)
            }
        ).flow
    }
}