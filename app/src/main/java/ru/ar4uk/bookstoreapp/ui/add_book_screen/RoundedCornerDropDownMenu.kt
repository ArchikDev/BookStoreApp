package ru.ar4uk.bookstoreapp.ui.add_book_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.mainScreen.utils.Categories
import ru.ar4uk.bookstoreapp.ui.theme.ButtonColor

@Composable
fun RoundedCornerDropDownMenu(
    defCategory: Int,
    onOptionSelected: (Int) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val categoriesList = stringArrayResource(R.array.category_array)
    val selectedOption = remember {
        mutableStateOf(categoriesList[Categories.FANTASY])
    }

    selectedOption.value = categoriesList[defCategory]

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(Color.White)
            .border(
                width = 1.dp,
                color = ButtonColor,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(15.dp)
            .clickable {
                expanded.value = true
            }
    ) {
        Text(
            text = selectedOption.value
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            categoriesList.forEachIndexed { index, title ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = title
                        )
                    },
                    onClick = {
                        selectedOption.value = title
                        expanded.value = false
                        onOptionSelected(index)
                    }
                )
            }
        }
    }
}