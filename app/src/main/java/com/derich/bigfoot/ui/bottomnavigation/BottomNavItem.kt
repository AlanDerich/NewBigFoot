package com.derich.bigfoot.ui.bottomnavigation

import com.derich.bigfoot.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    object Home : BottomNavItem("Home", R.drawable.ic_home,"home")
    object Loans: BottomNavItem("Loans",R.drawable.ic_loans,"loans")
    object Transactions: BottomNavItem("Transactions",R.drawable.ic_transactions,"transactions")
    object Account: BottomNavItem("Account",R.drawable.ic_account,"account")
    object AddTransaction: BottomNavItem("AddTransaction",R.drawable.baseline_add,"addTransaction")
}