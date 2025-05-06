package ru.ar4uk.bookstoreapp.ui.add_book_screen

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import ru.ar4uk.bookstoreapp.ui.login.LoginButton
import ru.ar4uk.bookstoreapp.ui.login.RoundedCornerTextField
import ru.ar4uk.bookstoreapp.ui.mainScreen.MainScreenViewModel
import ru.ar4uk.bookstoreapp.ui.theme.BoxFilterColor

@Composable
fun AddBookScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {},
    viewModel: AddBookViewModel = hiltViewModel()
) {
    val context = LocalContext.current.contentResolver

    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }

    val showLoadIndicator = remember {
        mutableStateOf(false)
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        navImageUrl.value = ""
        viewModel.selectedImageUri.value = uri
    }

    LaunchedEffect(Unit) {
        viewModel.setDefaultsData(navData)
        viewModel.uiState.collect { state ->
            when (state) {
                is MainScreenViewModel.MainUiState.Loading -> {
                    showLoadIndicator.value = true
                }
                is MainScreenViewModel.MainUiState.Success -> {
                    showLoadIndicator.value = false
                    onSaved()
                }
                is MainScreenViewModel.MainUiState.Error -> {
                    showLoadIndicator.value = false
                    Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Image(
        modifier = Modifier
            .fillMaxSize(),
        painter = rememberAsyncImagePainter(
            model = navImageUrl.value.ifEmpty { viewModel.selectedImageUri.value }
        ),
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
        RoundedCornerDropDownMenu(viewModel.selectedCategory.intValue) { selectedItemIndex ->
            viewModel.selectedCategory.intValue = selectedItemIndex
        }
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            text = viewModel.title.value,
            onValueChange = {
                viewModel.title.value = it
            },
            label = "Title"
        )
        Spacer(modifier = Modifier.height(15.dp))
        RoundedCornerTextField(
            singleLine = false,
            maxLines = 5,
            text = viewModel.description.value,
            onValueChange = {
                viewModel.description.value = it
            },
            label = "Description"
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = viewModel.price.value,
            onValueChange = {
                viewModel.price.value = it
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
            showLoadIndicator.value,
            onClick = {
                viewModel.uploadBook(navData)
            }
        )

    }
}

