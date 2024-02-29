package com.derich.bigfoot.ui.screens.home

import android.content.Context
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.common.composables.CommonVariables
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class ContributionsViewModel(
    private val firebaseDataSource: FirebaseDataSource
): ViewModel() {
    var members = MutableStateFlow<List<MemberDetails>>(listOf())


//    private val contributionsRepository: ContributionsHistoryRepository = ContributionsHistoryRepository()

//    var loadingContributions = mutableStateOf(false)
//    var loadingMemberDetails = mutableStateOf(false)
    init {
    viewModelScope.launch {
        firebaseDataSource.getMemberDetails().collect { memberDetails ->
            if (memberDetails.size!=members.value.size){
                updateUI(memberDetails)
            }
        }
    }
    }

    private fun updateUI(memberDetails: List<MemberDetails>) {
        members.value = memberDetails
    }


//    private fun getMemberDetails() {
//        viewModelScope.launch {
//            loadingMemberDetails.value = true
//            memberData.value = contributionsRepository.getMemberDetailsFromFirestore()
//            loadingMemberDetails.value = false
//        }
//    }

companion object{
    fun calculateContributionsDifference(totalAmount: Int) : Int {

        if (totalAmount<30440){
            //this formula is the old version which was being used to calculate the contributions when the contributions
            //we're ksh 20 per day
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val currentDate = sdf.format(Date())
            val currDate = sdf.parse("29/02/2024")
            val startDate = sdf.parse("31/12/2019")
            val diff: Long = currDate!!.time - startDate!!.time
            val daysRemaining: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return (totalAmount - (daysRemaining.toInt() * CommonVariables.PreviousContribution))
        }
        else{
            //this new formula calculates contributions from 1/3/2024 when the transactions changed to ksh 35 per day
            //totalAmount is now the total amount less the 30420
            val newTotalAmount = totalAmount- 30420
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val currentDate = sdf.format(Date())
            val currDate = sdf.parse(currentDate)
            val startDate = sdf.parse("29/02/2024")
            val diff: Long = currDate!!.time - startDate!!.time
            val daysRemaining: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return (newTotalAmount - (daysRemaining.toInt() * CommonVariables.CurrentContribution))

        }

    }
}
}
fun syncAllChanges(transactionsViewModel: TransactionsViewModel,
                   allMemberInfo: State<List<MemberDetails>>,
                   context: Context){
    transactionsViewModel.syncAllTransactions(allMemberInfo, context)

}

