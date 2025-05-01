package ru.ar4uk.bookstoreapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {
    val auth = remember {
        Firebase.auth
    }

    val errorState = remember {
        mutableStateOf("")
    }

    val emailState = remember { mutableStateOf("ml_serg@mail.ru") }
    val passwordState = remember { mutableStateOf("123456") }

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = painterResource(id = R.drawable.book_store_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoxFilterColor)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.books),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Books Store",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = emailState.value,
            onValueChange = {
                emailState.value = it
            },
            label = "Email"
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            isPassword = true,
            onValueChange = {
                passwordState.value = it
            },
            label = "Password"
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (errorState.value.isNotEmpty()) {
            Text(
                text = errorState.value,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
        LoginButton(
            text = "Sign In",
            onClick = {
                signIn(
                    auth = auth,
                    email = emailState.value,
                    passworrd = passwordState.value,
                    onSignInSuccess = { navData ->
                        onNavigateToMainScreen(navData)
                    },
                    onSignInFailure = { error ->
                        errorState.value = error
                    }
                )
            }
        )
        LoginButton(
            text = "Sign Up",
            onClick = {
                signUp(
                    auth = auth,
                    email = emailState.value,
                    passworrd = passwordState.value,
                    onSignUpSuccess = { navData ->
                        onNavigateToMainScreen(navData)

                    },
                    onSignUpFailure = { error ->
                        errorState.value = error
                    }
                )
            }
        )
    }
}

fun signUp(
    auth: FirebaseAuth,
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
    auth: FirebaseAuth,
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
