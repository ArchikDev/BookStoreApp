package ru.ar4uk.bookstoreapp.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth
) {

}