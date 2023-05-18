package com.derich.bigfoot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.derich.bigfoot.model.Response
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
import com.derich.bigfoot.ui.screens.login.AuthService
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.screens.login.PhoneLoginUI
import com.derich.bigfoot.ui.theme.BigFootTheme
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit


class LoginActivity: ComponentActivity() {
    companion object {
        var LoginActivity: LoginActivity? = null

//        fun getInstance(): LoginActivity? = LoginActivity
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val auth = FirebaseAuth.getInstance()
        val authServImp = AuthImplementation(this, auth)
        val authVM = AuthViewModel(authServImp, auth)
        LoginActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            BigFootTheme {
                Scaffold(
                    topBar = {
                        BigFutAppBar()
                    }
                ) { innerPadding ->
                    PhoneLoginUI(
                        navigateToHome = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        },
                        authVM,
                        restartLogin = {
                            authVM.resetAuthState()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LoginActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        LoginActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginActivity = null
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
class AuthImplementation(context: LoginActivity, auth: FirebaseAuth) : AuthService {
    private val mTag = AuthService::class.java.simpleName
    private val mAuth= auth
    private val mContext=context
    var verificationOtp: String = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null
    override var signUpState: MutableStateFlow<Response> =
        MutableStateFlow(Response.NotInitialized)
        private set


    private val authCallbacks = object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential:
                                             PhoneAuthCredential
        ) {
            Log.i(
                mTag,
                "onVerificationCompleted: Verification completed. ${context.getString(R.string .verification_complete)}"
            )
            signUpState.value =
                Response.Loading(message = context.getString(R.string.verification_complete))
            // Use obtained credential to sign in user
            signInWithAuthCredential(credential)
        }

        override fun onVerificationFailed(exception: FirebaseException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    signUpState.value =
                        Response.Error(
                            exception =
                            Exception(context.getString(R.string.verification_failed_try_again))
                        )

                }
                is FirebaseTooManyRequestsException -> {
                    signUpState.value =
                        Response.Error(
                            exception =
                            Exception(context.getString(R.string.quota_exceeded))
                        )

                }
                else -> {
                    signUpState.value = Response.Error(exception)

                }
            }

        }

        override fun onCodeSent(code: String, token:
        PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(code, token)
            verificationOtp = code
            resentToken = token
            signUpState.value = Response.Loading(
                message =
                context.getString(R.string.code_sent)
            )
        }

    }

    private val authBuilder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(auth)
        .setCallbacks(authCallbacks)
        .setActivity(context)
        .setTimeout(120L, TimeUnit.SECONDS)

    private fun signInWithAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(mTag, "signInWithAuthCredential:The sign in succeeded ")
                    signUpState.value =
                        Response.Success
                } else {


                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.e(mTag, mContext.getString(R.string.invalid_code))
                        signUpState.value =
                            Response.Error(exception = Exception(mContext.getString(R.string.invalid_code)))

                        return@addOnCompleteListener
                    } else {
                        signUpState.value = Response.Error(task.exception)
                        Log.e(mTag, "signInWithAuthCredential: Error ${task.exception?.message}")

                    }
                }

            }

    }

    override fun authenticate(phone: String) {
        signUpState.value =
            Response.Loading("${mContext.getString(R.string.code_will_be_sent)} $phone")
        val options = authBuilder
            .setPhoneNumber(phone)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        authCallbacks.onCodeSent(verificationId, token)
    }

    override fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithAuthCredential(credential)
    }



    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        authCallbacks.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        authCallbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun getUserPhone(): String {
        return mAuth.currentUser?.phoneNumber.orEmpty()
    }
}