package com.derich.bigfoot.ui.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.derich.bigfoot.LoginActivity
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel (
    private val firebaseAuth: FirebaseAuth)
    : ViewModel() {
    fun logOut(context: Context) {
        firebaseAuth.signOut()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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