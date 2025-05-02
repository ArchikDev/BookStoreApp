package ru.ar4uk.bookstoreapp.utils.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth
) {
    fun signUp(
        email: String,
        passworrd: String,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit
    ) {
        if (email.isBlank() || passworrd.isBlank()) {
            onSignUpFailure("Email and password cannot be empty")

            return
        }

        auth.createUserWithEmailAndPassword(email, passworrd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignUpSuccess(
                    MainScreenDataObject(
                        uid = task.result.user?.uid!!,
                        email = task.result.user?.email!!
                    )
                )
            }
            .addOnFailureListener {
                onSignUpFailure(it.message ?: "Sign Up Error")
            }
    }

    fun signIn(
        email: String,
        passworrd: String,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        if (email.isBlank() || passworrd.isBlank()) {
            onSignInFailure("Email and password cannot be empty")

            return
        }

        auth.signInWithEmailAndPassword(email, passworrd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSignInSuccess(
                    MainScreenDataObject(
                        uid = task.result.user?.uid!!,
                        email = task.result.user?.email!!
                    )
                )
            }
            .addOnFailureListener {
                onSignInFailure(it.message ?: "Sign In Error")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }
}