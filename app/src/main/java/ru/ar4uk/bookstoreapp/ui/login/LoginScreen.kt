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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun LoginScreen() {
    val auth = Firebase.auth

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = painterResource(id = R.drawable.book_store_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(BoxFilterColor)
    )
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value = passwordState.value,
            onValueChange = {
                passwordState.value = it
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {

        }) {
            Text(
                text = "Sign In"
            )
        }
        Button(onClick = {

        }) {
            Text(
                text = "Sign Up"
            )
        }
    }
}
