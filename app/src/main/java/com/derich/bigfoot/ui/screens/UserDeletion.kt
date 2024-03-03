package com.derich.bigfoot.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.derich.bigfoot.ui.screens.login.AuthViewModel

@Composable
fun DataDeletionRequest(authViewModel: AuthViewModel) {
    val activity = LocalContext.current as? Activity
    var phoneNumber by remember { mutableStateOf("") }
    var deletionReason by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp),
            placeholder = { Text(text = "Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = deletionReason,
            onValueChange = { deletionReason = it },
            textStyle = TextStyle(fontSize = 16.sp),
            placeholder = { Text(text = "Reason for Deletion") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = {
                // Create an email Intent with pre-filled subject and body
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:alangitonga15@gmail.com"))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Data Deletion Request")
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Phone Number: $phoneNumber\n\nReason for Deletion: $deletionReason")
                try {
                    if (activity != null) {
                        authViewModel.logOutAndExit(activity = activity)
                    }
                    context.startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    // Handle the case where there's no email app
                    // You can display a message or take appropriate action here
                }
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Send")
        }
    }
}

@Preview
@Composable
fun DataDeletionRequestPreview() {
//    DataDeletionRequest()
}