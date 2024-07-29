package com.yusuf.component.auth_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yusuf.feature.R


@Composable
fun PasswordFieldComponent(stateValue:String,label: String,onValueChange: (String) -> Unit , painterResource : Painter){

    var password = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        label = { Text(text = label) },
        value = password.value,
        colors =  OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.LightGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        onValueChange = {
            password.value = it
            onValueChange(it)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (passwordVisibility.value){
                R.drawable.ic_hide_password
            } else {
                R.drawable.ic_show_password
            }

            var description = if(passwordVisibility.value){
                "Hide Password"
            } else {
                "Show Password"
            }

            val iconPainter = painterResource(id = iconImage)
            IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                Icon(painter = iconPainter, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}