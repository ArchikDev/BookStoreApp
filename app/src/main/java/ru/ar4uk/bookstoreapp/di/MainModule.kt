package ru.ar4uk.bookstoreapp.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ar4uk.bookstoreapp.utils.firebase.AuthManager
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideFirebaseManager(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): FireStoreManager {
        return FireStoreManager(auth, db)
    }

    @Provides
    @Singleton
    fun provideAuthManager(
        auth: FirebaseAuth
    ): AuthManager {
        return AuthManager(auth)
    }
}