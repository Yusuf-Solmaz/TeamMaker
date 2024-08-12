package com.yusuf.component

import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.RED

@Composable
fun ConfirmationDialog(
    onDismiss: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = "Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            AuthButtonComponent(
                value = "Yes",
                onClick = {
                    onConfirm()
                    onDismiss(false)
                },
                modifier = Modifier.width(60.dp),
                fillMaxWidth = false,
                heightIn = 40.dp,
                firstColor = RED,
                secondColor = DARK_RED
            )
        },
        dismissButton = {
            AuthButtonComponent(
                value = "No",
                onClick = {
                    onDismiss(false)
                },
                modifier = Modifier.width(60.dp),
                fillMaxWidth = false,
                heightIn = 40.dp
            )
        },
        modifier = Modifier,
        containerColor = Color.White
    )
}


