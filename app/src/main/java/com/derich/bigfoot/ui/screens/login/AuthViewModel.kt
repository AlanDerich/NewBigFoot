package com.derich.bigfoot.ui.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.derich.bigfoot.MainActivity
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel (
    private val firebaseAuth: FirebaseAuth)
    : ViewModel() {
    fun logOut(context: Context) {
        firebaseAuth.signOut()
        Toast.makeText(context, "Successfully logged out.", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, PhoneAuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }
    fun logIn(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }
    fun logOutAndExit(activity: Activity){
        firebaseAuth.signOut()
        activity.finish()
    }
    fun deleteAccount(context: Context, navController: NavController) {
        navController.navigate(BottomNavItem.DeleteAccount.screenRoute)
    }

}