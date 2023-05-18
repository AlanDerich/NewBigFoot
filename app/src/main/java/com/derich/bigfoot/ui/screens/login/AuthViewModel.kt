package com.derich.bigfoot.ui.screens.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.LoginActivity
import com.derich.bigfoot.model.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (private val accountService: AuthService,
            private val firebaseAuth: FirebaseAuth)
    : ViewModel() {

    val signUpState: MutableStateFlow<Response> = accountService.signUpState

    private val _number: MutableStateFlow<String> = MutableStateFlow("")
    val number: StateFlow<String> get() = _number
    private val _code: MutableStateFlow<String> = MutableStateFlow("")
    val code: StateFlow<String> get() = _code
    fun authenticatePhone(phone: String) {
        accountService.authenticate(phone)
    }
    fun logOut(context: Context) {
        firebaseAuth.signOut()
        resetAuthState()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }
    fun resetAuthState() {
        signUpState.value = Response.NotInitialized
        _code.value = ""
    }

    fun onNumberChange(number: String) {
        _number.value = number
    }

    fun onCodeChange(code: String) {
        _code.value = code.take(6)
    }

    fun verifyOtp(code: String) {
        viewModelScope.launch {
            accountService.onVerifyOtp(code)
        }
    }
//    fun uploadImageToDb() {}


}