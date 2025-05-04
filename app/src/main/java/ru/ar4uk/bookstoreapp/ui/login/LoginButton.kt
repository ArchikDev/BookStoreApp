package ru.ar4uk.bookstoreapp.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.ar4uk.bookstoreapp.ui.theme.ButtonColor

@Composable
fun LoginButton(
    text: String,
    showLoadIndicator: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor
        )
    ) {
        if (showLoadIndicator) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp)
            )
        } else {
            Text(
                text = text
            )
        }

    }
}