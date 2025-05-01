package ru.ar4uk.bookstoreapp.ui.login

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.ar4uk.bookstoreapp.R
import ru.ar4uk.bookstoreapp.ui.theme.ButtonColor

@Composable
fun RoundedCornerTextField(
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    text: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    TextField(
        value = text,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(25.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ButtonColor, RoundedCornerShape(25.dp)),
        label = {
            Text(text = label, color = Color.Gray)
        },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (isPasswordVisible || !isPassword) VisualTransformation.None
                               else PasswordVisualTransformation(),
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    Icon(
                        painter = if (isPasswordVisible)
                            painterResource(R.drawable.visibility_off)
                        else painterResource(R.drawable.visibility),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

        }
    )
}