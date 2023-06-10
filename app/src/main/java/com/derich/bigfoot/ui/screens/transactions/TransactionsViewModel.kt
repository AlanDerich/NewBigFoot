package com.derich.bigfoot.ui.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransactionsViewModel (
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {
    var uploadValue= MutableStateFlow(0)
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
        navController.navigate(BottomNavItem.AddTransaction.screen_route)
    }
    fun launchTransactionScreen(navController: NavController) {
        navController.navigate(BottomNavItem.Transactions.screen_route)
    }
    fun addTransaction(transactionDetails: Transactions, previousAmount: Int, memberPhone: String) {
        uploadValue.update { 5 }
        var transactionStatus = firebaseDataSource.uploadToTransactions(transactionDetails)
        var memberDetailsStatus = firebaseDataSource.updateContributionsDetails(memberPhone,
            transactionDetails.depositFor,
            calculateResultingDate(previousAmount + transactionDetails.transactionAmount),
            newUserAmount = (previousAmount + (transactionDetails.transactionAmount)).toString())
        if (transactionStatus.value==1 && memberDetailsStatus.value==1){
            uploadValue.update{1}
        }
        else if (memberDetailsStatus.value==2 || memberDetailsStatus.value==2){
            uploadValue.update{2}
        }
        else{
            uploadValue.update { 3 }
        }
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
    private fun calculateResultingDate(newUserAmount: Int): String {
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