package ru.ar4uk.bookstoreapp.ui.add_book_screen

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
import androidx.compose.foundation.layout.size
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
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.login.LoginButton
import ru.ar4uk.bookstoreapp.ui.login.RoundedCornerTextField
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun AddBookScreen() {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = painterResource(id = R.drawable.book_store_bg),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alpha = 0.4f
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
                .size(90.dp),
            painter = painterResource(id = R.drawable.books),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Add new book",
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = title.value,
            onValueChange = {
                title.value = it
            },
            label = "Title"
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            singleLine = false,
            maxLines = 5,
            text = description.value,
            onValueChange = {
                description.value = it
            },
            label = "Description"
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = price.value,
            onValueChange = {
                price.value = it
            },
            label = "Price"
        )
        Spacer(modifier = Modifier.height(10.dp))

        LoginButton(
            text = "Select image",
            onClick = {
            }
        )
        LoginButton(
            text = "Save",
            onClick = {

            }
        )

    }
}