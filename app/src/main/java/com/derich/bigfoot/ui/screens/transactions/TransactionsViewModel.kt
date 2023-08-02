package com.derich.bigfoot.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransactionsViewModel (
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {
    var uploadValue= MutableStateFlow(0)
    var uploadStatus = MutableStateFlow(false)
    var uploadError = MutableStateFlow("")
    var transactions = MutableStateFlow<List<Transactions>>(listOf())

    init {
        viewModelScope.launch {
            firebaseDataSource.getAllTransactions().collect { transactionsDetails ->
                if (transactionsDetails.size!=transactions.value.size) {
                    updateUI(transactionsDetails)
                }
            }
        }
    }
    private fun updateUI(transactionsList: List<Transactions>) {
        transactions.value = transactionsList
    }
    fun launchAddTransactionScreen(navController: NavController) {
        navController.navigate(BottomNavItem.AddTransaction.screenRoute)
    }
    fun launchTransactionScreen(navController: NavController) {
        navController.navigate(BottomNavItem.Transactions.screenRoute)
    }
    fun addTransaction(transactionDetails: Transactions, previousAmount: Int, memberPhone: String): Task<Void> {
            return firebaseDataSource.uploadTransactions(transactionDetails)
    }
    fun updateContributions(memberPhoneNumber: String,
                                    memberFullNames: String,
                                    resultingDate: String,
                                    newUserAmount: String): Task<Void> {
            return firebaseDataSource.updateContributions(memberPhoneNumber,memberFullNames,resultingDate, newUserAmount)
    }
//    fun launchTransactionScreen(navController: NavController) {
//        navController.navigate(BottomNavItem.Transactions.screen_route)
//    }
//    fun addTransaction(transactionDetails: Transactions,
//                       previousAmount: Int,
//                       memberPhone: String) {
//        //check if upload job is complete or not
//        val waitingForResponse = viewModelScope.launch {
//            transactionUploadStatus = repository.uploadTransactionToDb(transactionDetails)
//            contUploadStatus = repository.updateMemberContributions(
//                memberPhoneNumber = memberPhone,
//                memberFullNames = transactionDetails.depositFor,
//                resultingDate = calculateResultingDate(previousAmount + (transactionDetails.transactionAmount)),
//                newUserAmount = (previousAmount + (transactionDetails.transactionAmount)).toString())
//        }.isActive
//        uploadStatus.value = waitingForResponse
//    }
    fun calculateResultingDate(newUserAmount: Int): String {
        val totalDays: Int = (newUserAmount / 20)
        //add number of days to the start date
        var sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val c: Calendar = Calendar.getInstance()
        c.time = sdf.parse("31/12/2019")!!
        c.add(Calendar.DATE, totalDays)
        sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return sdf.format(Date(c.timeInMillis))
    }
}