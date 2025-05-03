package ru.ar4uk.bookstoreapp.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.ktx.auth
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.custom.MyDialog
import ru.ar4uk.bookstoreapp.ui.login.data.MainScreenDataObject
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAccountState()
        viewModel.getEmail()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveLastEmail()
            viewModel.passwordState.value = ""
        }
    }

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
        if (viewModel.currentUser.value == null) {
            RoundedCornerTextField(
                text = viewModel.emailState.value,
                onValueChange = {
                    viewModel.emailState.value = it
                },
                label = "Email"
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (!viewModel.resetPasswordState.value) {
                RoundedCornerTextField(
                    text = viewModel.passwordState.value,
                    isPassword = true,
                    onValueChange = {
                        viewModel.passwordState.value = it
                    },
                    label = "Password"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (viewModel.errorState.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
            if (!viewModel.resetPasswordState.value) {
                LoginButton(
                    text = "Sign In",
                    onClick = {
                        viewModel.signIn(
                            onSignInSuccess = { navData ->
                                onNavigateToMainScreen(navData)
                            }
                        )
                    }
                )
            }

            LoginButton(
                text = if (viewModel.resetPasswordState.value) "Reset Password" else "Sign Up",
                onClick = {
                    viewModel.signUp(
                        onSignUpSuccess = { navData ->
                            onNavigateToMainScreen(navData)
                        }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!viewModel.resetPasswordState.value) {
                Text(
                    text = "Forget password?",
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            viewModel.errorState.value = ""
                            viewModel.resetPasswordState.value = true
                        }
                )
            }
        } else {
            LoginButton(
                text = "Enter",
                onClick = {
                    onNavigateToMainScreen(
                        MainScreenDataObject(
                            viewModel.currentUser.value!!.uid,
                            viewModel.currentUser.value!!.email!!
                        )
                    )
                }
            )
            LoginButton(
                text = "Sign Out",
                onClick = {
                    viewModel.signOut()
                }
            )
        }
        MyDialog(
            showDialog = viewModel.showResetPasswordDialog.value,
            onDismiss = {
                viewModel.showResetPasswordDialog.value = false
            },
            onConfirm = {
                viewModel.showResetPasswordDialog.value = false
            },
            message = stringResource(R.string.reset_password_message)
        )
    }
}




