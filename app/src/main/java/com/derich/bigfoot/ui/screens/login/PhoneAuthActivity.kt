package com.derich.bigfoot.ui.screens.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.derich.bigfoot.ui.theme.BigFootTheme
import com.google.firebase.auth.FirebaseAuth

class PhoneAuthActivity : ComponentActivity() {
    private val authVm = AuthViewModel(FirebaseAuth.getInstance())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigFootTheme {
                // A surface container using the
                // 'background' color from the theme
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    // on the below line we are specifying the theme as the scaffold.
                    PhoneAuthScreen(authVm)
                }
            }
        }
    }
}
