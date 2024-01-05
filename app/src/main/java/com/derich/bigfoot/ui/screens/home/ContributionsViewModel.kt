package com.derich.bigfoot.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.firebase.FirebaseDataSource
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

    fun calculateContributionsDifference(totalAmount: Int) : Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        val currDate = sdf.parse("29/02/2024")
        val startDate = sdf.parse("31/12/2019")
        val diff: Long = currDate!!.time - startDate!!.time
        val daysRemaining: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        return (totalAmount - (daysRemaining.toInt() * 20))
    }
}