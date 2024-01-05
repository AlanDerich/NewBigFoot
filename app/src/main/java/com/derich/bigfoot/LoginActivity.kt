package com.derich.bigfoot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult


class LoginActivity: AppCompatActivity() {

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }
    // [END auth_fui_create_launcher]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
//        createSignInIntent()
        themeAndLogo()
//        throw RuntimeException("Test Crash") // Force a crash
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("Back button pressed")
                finish()
            }
        })
    }

    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.d("Firebase Login", result.resultCode.toString())
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun themeAndLogo() {
//        val providers = emptyList<AuthUI.IdpConfig>()
        val providers = arrayListOf(
            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
//            AuthUI.IdpConfig.TwitterBuilder().build(),
        )

        // [START auth_fui_theme_logo]
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.bigfut1) // Set logo drawable
            .setTheme(R.style.AppTheme) // Set theme
//            .setTosAndPrivacyPolicyUrls(
//                "https://example.com/terms.html",
//                "https://example.com/privacy.html",
//            )
            .build()

        signInLauncher.launch(signInIntent)
        // [END auth_fui_theme_logo]
    }
}