package ru.ar4uk.bookstoreapp.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.utils.firebase.AuthManager
import ru.ar4uk.bookstoreapp.utils.store.StoreManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager
): ViewModel() {
    val errorState =  mutableStateOf("")

    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    val resetPasswordState = mutableStateOf(false)

    val currentUser = mutableStateOf<FirebaseUser?>(null)

    val showResetPasswordDialog = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (MainScreenDataObject) -> Unit
    ) {
        errorState.value = ""
        authManager.signIn(
            emailState.value,
            passwordState.value,
            onSignInSuccess = { navData ->
                onSignInSuccess(navData)
            },
            onSignInFailure = { error ->
                errorState.value = error
            }
        )
    }

    fun signUp(
        onSignUpSuccess: (MainScreenDataObject) -> Unit
    ) {
        errorState.value = ""
        if (resetPasswordState.value) {
            authManager.resetPassword(
                emailState.value,
                onResetPasswordSuccess = {
                    resetPasswordState.value = false
                    showResetPasswordDialog.value = true
                },
                onResetPasswordFailure = { errorMessage ->
                    errorState.value = errorMessage
                }
            )
        } else {
            authManager.signUp(
                emailState.value,
                passwordState.value,
                onSignUpSuccess = { navData ->
                    onSignUpSuccess(navData)
                },
                onSignUpFailure = { error ->
                    errorState.value = error
                }
            )
        }

    }

    fun saveLastEmail() {
        storeManager.saveString(
            StoreManager.EMAIL_KEY,
            emailState.value
        )
    }

    fun getEmail() {
        emailState.value = storeManager.getString(StoreManager.EMAIL_KEY, "")
    }

    fun getAccountState() {
        currentUser.value = authManager.getCurrentUser()
    }

    fun signOut() {
        authManager.signOut()
        currentUser.value = null
    }

}