package ru.ar4uk.bookstoreapp.ui.mainScreen.top_app_bar

import android.icu.text.CaseMap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.ar4uk.bookstoreapp.ui.theme.DarkBlue
import ru.ar4uk.bookstoreapp.ui.theme.Purple80
import ru.ar4uk.bookstoreapp.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String
) {
    var targetState by remember {
        mutableStateOf(false)
    }
    var expandedState by remember {
        mutableStateOf(false)
    }
    var queryText by remember {
        mutableStateOf("")
    }

    Crossfade(targetState = targetState) { target ->
        if (target) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        colors = TextFieldDefaults.colors(
                            focusedTrailingIconColor = DarkBlue,
                            unfocusedIndicatorColor = DarkBlue
                        ),
                        query = queryText,
                        placeholder = {
                            Text(
                                text = "Search..."
                            )
                        },
                        onQueryChange = {
                            queryText = it
                        },
                        onSearch = {

                        },
                        expanded = expandedState,
                        onExpandedChange = {
                            expandedState = it
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                expandedState = false
                                targetState = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }
                    )
                },
                expanded = expandedState,
                onExpandedChange = {
                    expandedState = it
                },
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(5) {
                            Text(
                                text = "Item $it",
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                },
                colors = SearchBarDefaults.colors(
                    containerColor = PurpleGrey80,
                    dividerColor = DarkBlue
                )
            )
        } else {
            TopAppBar(
                title = {
                    Text(
                        text = title
                    )
                },
                actions = {
                    IconButton(onClick = {
                        targetState = true
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurpleGrey80,
                    titleContentColor = DarkBlue,
                    actionIconContentColor = DarkBlue
                )
            )
        }

    }
}