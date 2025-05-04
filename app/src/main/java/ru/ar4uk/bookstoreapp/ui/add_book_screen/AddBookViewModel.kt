package ru.ar4uk.bookstoreapp.ui.add_book_screen

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.ar4uk.bookstoreapp.data.Book
import ru.ar4uk.bookstoreapp.ui.add_book_screen.data.AddScreenObject
import ru.ar4uk.bookstoreapp.ui.mainScreen.MainScreenViewModel.MainUiState
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories
import ru.ar4uk.bookstoreapp.utils.firebase.FireStoreManager
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val fireStoreManager: FireStoreManager
): ViewModel() {
    val title = mutableStateOf("")
    val description = mutableStateOf("")
    val price =  mutableStateOf("")
    var selectedCategory =  mutableIntStateOf(Categories.FANTASY)
    val selectedImageUri = mutableStateOf<Uri?>(null)

    private val _uiState = MutableSharedFlow<MainUiState>()
    val uiState = _uiState.asSharedFlow()

    private fun sendUiState(state: MainUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun setDefaultsData(navData: AddScreenObject) {
        title.value = navData.title
        description.value = navData.description
        price.value = navData.price
        selectedCategory.intValue = navData.categoryIndex
    }

    fun uploadBook(
        navData: AddScreenObject
    ) {
        sendUiState(MainUiState.Loading)
        val book = Book(
            id = navData.id,
            title = title.value,
            description = description.value,
            price = price.value,
            category = selectedCategory.intValue
        )

        fireStoreManager.saveBookImage(
            oldImageUrl = navData.imageUrl,
            uri = selectedImageUri.value,
            book = book,
            onSaved = {
                sendUiState(MainUiState.Success)
            },
            onError = {
                sendUiState(MainUiState.Error(it))
            },
        )
    }



}