package com.derich.bigfoot.ui.screens.transactions

import android.content.Context
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.derich.bigfoot.ui.common.composables.calculateResultingDate
import com.derich.bigfoot.ui.common.composables.showMessage
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel (
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {
    var transactions = MutableStateFlow<List<Transactions>>(listOf())

    fun collectTransactionsFromDB() {
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
        navController.popBackStack()
        navController.navigate(BottomNavItem.AddTransaction.screenRoute)
    }
    fun launchTransactionScreen(navController: NavController) {
        navController.popBackStack()
        navController.navigate(BottomNavItem.Transactions.screenRoute)
    }
    fun addTransaction(transactionDetails: Transactions): Task<Void> {
            return firebaseDataSource.uploadTransactions(transactionDetails)
    }
    fun updateContributions(memberPhoneNumber: String,
                                    memberFullNames: String,
                                    resultingDate: String,
                                    newUserAmount: String): Task<Void> {
            return firebaseDataSource.updateContributions(memberPhoneNumber,memberFullNames,resultingDate, newUserAmount)
    }
    fun updateProfilePic(memberPhoneNumber: String,
                         memberFullNames: String,
                         newUserProfileUrl: String): Task<Void> {
        return firebaseDataSource.updateProfPic(memberPhoneNumber,memberFullNames, newUserProfileUrl)
    }

    fun syncAllTransactions(allMemberInfo: State<List<MemberDetails>>, mContext: Context) {
            for (memberInfo in allMemberInfo.value){
                val resultingDate = calculateResultingDate(memberInfo.totalAmount.toInt())
                //first check if the contributions date is correct or not
                if (memberInfo.contributionsDate != resultingDate){
                    val task = updateContributions(memberInfo.phoneNumber,
                        memberInfo.fullNames,
                        resultingDate,
                        memberInfo.totalAmount)
                    task.addOnSuccessListener {
                        //do this if the upload was successful
                        mContext.showMessage("The details for ${memberInfo.fullNames} was updated successfully!")

                    }
                }

            }
    }

}
