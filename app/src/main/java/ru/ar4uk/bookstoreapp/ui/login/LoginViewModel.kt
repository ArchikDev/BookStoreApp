package ru.ar4uk.bookstoreapp.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ar4uk.bookstoreapp.utils.firebase.AuthManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

}