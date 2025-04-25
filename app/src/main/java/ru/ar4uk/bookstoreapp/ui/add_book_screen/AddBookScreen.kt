package ru.ar4uk.bookstoreapp.ui.add_book_screen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.login.LoginButton
import ru.ar4uk.bookstoreapp.ui.login.RoundedCornerTextField
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun AddBookScreen(
    onSaved: () -> Unit = {}
) {
    var selectedCategory = "Bestsellers"
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }

    val firestore = remember { Firebase.firestore }
    val storage = remember { Firebase.storage }


    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = rememberAsyncImagePainter(model = selectedImageUri.value),
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
        RoundedCornerDropDownMenu { selectedItem ->
            selectedCategory = selectedItem
        }
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
                imageLauncher.launch("image/*")
            }
        )
        LoginButton(
            text = "Save",
            onClick = {
                saveBookImage(
                    selectedImageUri.value!!,
                    storage,
                    firestore,
                    Book(
                        title = title.value,
                        description = description.value,
                        price = price.value,
                        category = selectedCategory,
                        imageUrl = price.value,
                    ),
                    onSaved = {
                        onSaved()
                    },
                    onError = {}
                )
            }
        )

    }
}

private fun saveBookImage(
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage.reference
        .child("book_images")
        .child("image_$timeStamp.jpg")
    val uploadTask = storageRef.putFile(uri)

    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { url ->
            saveBookToFireStore(
                firestore = firestore,
                url = url.toString(),
                book = book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError()
                }
            )
        }
    }

}

private fun saveBookToFireStore(
    firestore: FirebaseFirestore,
    url: String,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val db = firestore.collection("books")
    val id = db.document().id

    db.document(id)
        .set(
            book.copy(id = id, imageUrl = url)
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener {
            onError()
        }
}