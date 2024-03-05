package com.derich.bigfoot.ui.screens.login

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.derich.bigfoot.ui.common.composables.CommonVariables.showMessage
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit


@Composable
fun PhoneAuthScreen(authVm: AuthViewModel) {
    val phoneNumber = rememberSaveable {
        mutableStateOf("")
    }

    val otp = rememberSaveable {
        mutableStateOf("")
    }

    val verificationID = rememberSaveable {
        mutableStateOf("")
    }//Need this to get credentials

    val codeSent = rememberSaveable {
        mutableStateOf(false)
    }//Optional- Added just to make consistent UI

    val loading = rememberSaveable {
        mutableStateOf(false)
    }//Optional

    val context = LocalContext.current

    val mAuth: FirebaseAuth = Firebase.auth



    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(//import androidx.compose.material3.TextField
            enabled = !codeSent.value && !loading.value,
            value = phoneNumber.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { if (it.length <= 13) phoneNumber.value = it },
            placeholder = { Text(text = "Enter your phone number") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            supportingText = {
                Text(
                    text = "${phoneNumber.value.length} / 13",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AnimatedVisibility(
            visible = !codeSent.value,
            exit = scaleOut(
                targetScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            ),
            enter = scaleIn(
                initialScale = 0.5f,
                animationSpec = tween(durationMillis = 500, delayMillis = 100)
            )
        ) {
            Button(
                enabled = !loading.value && !codeSent.value,
                onClick = {
                    if (TextUtils.isEmpty(phoneNumber.value) || phoneNumber.value.length < 10) {
                        context.showMessage("Enter a valid phone number")
                    } else {
                        loading.value = true
                        val number = if (phoneNumber.value.startsWith("+254")) {
                            phoneNumber.value
                        } else {
                            "+254${phoneNumber.value}"
                        }
                        sendVerificationCode(number, mAuth, context as Activity, callbacks)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Generate OTP", modifier = Modifier.padding(8.dp))
            }
        }

        //display OTP UI

        AnimatedVisibility(
            visible = codeSent.value,
            // Add same animation here
        ) {
            Column {
                TextField(
                    enabled = !loading.value,
                    value = otp.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { if (it.length <= 6) otp.value = it },
                    placeholder = { Text(text = "Enter your otp") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "${otp.value.length} / 6",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    enabled = !loading.value,
                    onClick = {
                        if (TextUtils.isEmpty(otp.value) || otp.value.length < 6) {
                            context.showMessage("Please enter a valid OTP")
                        } else {
                            loading.value = true
                            //This is the main part where we verify the OTP
                            val credential: PhoneAuthCredential =
                                PhoneAuthProvider.getCredential(
                                    verificationID.value, otp.value
                                )//Get credential object
                            mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(context as Activity) { task ->
                                    if (task.isSuccessful) {
                                        //Code after auth is successful
                                        authVm.logIn(context)
                                    } else {
                                        loading.value = false
                                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                            context.showMessage("Verification failed..")
                                            Log.d("Verification failed..", (task.exception as FirebaseAuthInvalidCredentialsException).message.toString())
                                            if ((task.exception as FirebaseAuthInvalidCredentialsException).message?.contains(
                                                    "expired"
                                                ) == true
                                            ) {//If code is expired then get a chance to resend the code
                                                codeSent.value = false
                                                otp.value = ""
                                            }
                                        }
                                    }
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Verify OTP", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

    if (loading.value) {
        LinearProgressIndicator(modifier = Modifier.wrapContentSize(Alignment.Center))
    }



    callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            context.showMessage("Verification successful..")
            loading.value = false
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            context.showMessage("Verification failed.. ${p0.message}")
            loading.value = false
        }

        override fun onCodeSent(
            verificationId: String,
            p1: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, p1)
            verificationID.value = verificationId
            codeSent.value = true
            loading.value = false
        }
    }

}
private fun sendVerificationCode(
    number: String,
    auth: FirebaseAuth,
    activity: Activity,
    callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber(number)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callbacks)
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}
lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks