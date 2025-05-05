package ru.ar4uk.bookstoreapp.ui.mainScreen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories
import ru.ar4uk.bookstoreapp.ui.theme.DarkBlue
import ru.ar4uk.bookstoreapp.ui.theme.DarkTransparentBlue
import ru.ar4uk.bookstoreapp.ui.theme.GrayLight

@Composable
fun DrawerBody(
    onAdmin: (Boolean) -> Unit,
    onAdminClick: () -> Unit,
    onFavsClick: () -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
) {

    val isAdminState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isAdmin { isAdmin ->
            isAdminState.value = isAdmin
            onAdmin(isAdmin)
        }
    }

    val categoriesList = stringArrayResource(id = R.array.category_array)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.book_store_bg),
            contentDescription = null,
            alpha = 0.2f,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GrayLight)
            )
            DrawerListItem(
                title = stringResource(R.string.favs),
                onItemClick = {
                    onFavsClick()
                }
            )
            DrawerListItem(
                title = stringResource(R.string.all),
                onItemClick = {
                    onCategoryClick(Categories.ALL)
                }
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(categoriesList) { index, item ->
                    DrawerListItem(
                        title = item,
                        onItemClick = {
                            onCategoryClick(index)
                        }
                    )
                }
            }
            if (isAdminState.value) {
                Button(
                    onClick = {
                        onAdminClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkTransparentBlue
                    )
                ) {
                    Text(
                        text = "Admin Panel"
                    )
                }
            }

        }
    }
}

fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val uid = Firebase.auth.currentUser!!.uid

    Firebase.firestore.collection("admin")
        .document(uid).get().addOnSuccessListener {
            onAdmin(it.get("isAdmin") as Boolean)
        }
}