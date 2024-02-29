package com.derich.bigfoot.ui.bottomnavigation

import com.derich.bigfoot.R

sealed class BottomNavItem(var title:String, var icon:Int, var screenRoute:String){

    data object Home : BottomNavItem("Home", R.drawable.ic_home,"home")
    data object Loans: BottomNavItem("Loans",R.drawable.ic_loans,"loans")
    data object Transactions: BottomNavItem("Transactions",R.drawable.ic_transactions,"transactions")
    data object Account: BottomNavItem("Account",R.drawable.ic_account,"account")
    data object AddTransaction: BottomNavItem("AddTransaction",R.drawable.baseline_add,"addTransaction")
    data object DeleteAccount: BottomNavItem("DeleteAccount", R.drawable.ic_account, "deleteAccount")
    data object ImageUploader: BottomNavItem("ImageUploader", R.drawable.ic_account, "imageUploader")
}