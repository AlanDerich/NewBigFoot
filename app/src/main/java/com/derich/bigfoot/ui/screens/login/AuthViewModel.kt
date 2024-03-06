package com.derich.bigfoot.ui.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.derich.bigfoot.MainActivity
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.derich.bigfoot.ui.common.composables.CommonVariables.showMessage
import com.derich.bigfoot.ui.screens.account.ExportDataActivity
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel (
    private val firebaseAuth: FirebaseAuth)
    : ViewModel() {
    fun logOut(context: Context) {
        firebaseAuth.signOut()
        context.showMessage("Successfully logged out.")
        val intent = Intent(context, PhoneAuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)

    }
    //navigate to export data to excel screen
    fun navigateToExportDataActivity(context: Context) {
        val intent = Intent(context, ExportDataActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
    fun deleteAccount(navController: NavController) {
        navController.navigate(BottomNavItem.DeleteAccount.screenRoute)
    }

}