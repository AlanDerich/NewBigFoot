package com.derich.bigfoot.ui.screens.transactions

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.derich.bigfoot.ui.common.composables.CommonVariables
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
                        Toast.makeText(mContext, "The details for ${memberInfo.fullNames} was updated successfully!", Toast.LENGTH_SHORT).show()

                    }
                }

            }
    }
    companion object{
        fun calculateResultingDate(newUserAmount: Int): String {
            //do this if the user has contributed less than 30440 which is 29/02/2024
            if (newUserAmount <30440){
                val totalDays: Int = (newUserAmount / CommonVariables.PreviousContribution)
                //add number of days to the start date
                var sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val c: Calendar = Calendar.getInstance()
                c.time = sdf.parse("31/12/2019")!!
                c.add(Calendar.DATE, totalDays)
                sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                return sdf.format(Date(c.timeInMillis))
            }
            else{
                //do this if the user has contributed more than 30440
                val totalDays: Int = ((newUserAmount-30420) / CommonVariables.CurrentContribution)
                //add number of days to the start date
                var sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val c: Calendar = Calendar.getInstance()
                c.time = sdf.parse("29/02/2024")!!
                c.add(Calendar.DATE, totalDays)
                sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                return sdf.format(Date(c.timeInMillis))
            }
        }
    }

}
