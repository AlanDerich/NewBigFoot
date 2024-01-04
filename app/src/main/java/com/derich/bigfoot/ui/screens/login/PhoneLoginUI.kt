package com.derich.bigfoot.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhoneLoginUI(
    navigateToHome: () -> Unit,
    viewModel: AuthViewModel,
    restartLogin: () -> Unit,
    modifier: Modifier = Modifier
)
 {
//    val context = LocalContext.current
//
//    // Sign up state
////    val uiState by viewModel.signUpState
//        .collectAsState(initial = Response.NotInitialized)
//    // SMS code
//    val code by viewModel.code.collectAsState(initial = "")
//
//    // Phone number
//    val phone by viewModel.number.collectAsState(initial = "")
//
//    val focusManager = LocalFocusManager.current
////    if (viewModel.authState.currentUser != null){
////        LaunchedEffect(key1 = "navigateHome") {
////            navigateToHome()
////        }
////        Log.d("Code", "Previously logged in")
////    }
////    else{
//    when (uiState) {
//        // Nothing happening yet
//        is Response.NotInitialized -> {
//            EnterPhoneNumberUI(
//                onClick = {
//                    focusManager.clearFocus()
//                    viewModel.authenticatePhone(phone)
//                },
//                phone = phone,
//                onPhoneChange = viewModel::onNumberChange,
//                onDone = {
//                    focusManager.clearFocus()
//                    viewModel.authenticatePhone(phone)
//                }
//            )
//        }
//
//        // State loading
//        is Response.Loading -> {
//            val text = (uiState as Response.Loading).message
//            if (text == context.getString(R.string.code_sent)) {
//
//                // If the code is sent, display the screen for code
//                EnterCodeUI(
//                    code = code,
//                    onCodeChange = viewModel::onCodeChange,
//                    phone = phone,
//                    onGo = {
//                        Log.d("Code Sent", "The code is $code")
//                        focusManager.clearFocus()
//                        viewModel.verifyOtp(code)
//                    },
//                onNext = {
//                    Log.d("Code Sent", "The code is $code")
//                    focusManager.clearFocus()
//                    viewModel.verifyOtp(code)
//                })
//
//            } else {
//
//                // If the loading state is different form the code sent state,
//                // show a progress indicator
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    CircularProgressIndicator()
//                    text?.let {
//                        Text(
//                            it, modifier = Modifier.fillMaxWidth(),
//                            textAlign = TextAlign.Center
//                        )
//                    }
//
//                }
//
//
//            }
//
//        }
//
//        // If it is the error state, show the error UIx
//        is Response.Error -> {
//            val throwable = (uiState as Response.Error).exception!!
//            ErrorUi(exc = throwable, onRestart = restartLogin)
//            Log.d("ErrorLogin", "The Sign in failed ${throwable.message}")
//        }
//
//        // You can navigate when the auth process is successful
//        is Response.Success -> {
//            Log.d("Code", "The Sign in was successful")
//            viewModel.resetAuthState()
//            LaunchedEffect(key1 = "navigateToHome") {
//                navigateToHome()
//            }
//        }
//    }
//}
        }
