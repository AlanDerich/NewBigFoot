package com.derich.bigfoot.ui.screens.home

import android.content.Context
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class ContributionsViewModel(
    private val firebaseDataSource: FirebaseDataSource
): ViewModel() {
    var members = MutableStateFlow<List<MemberDetails>>(listOf())
    init {
    }
    fun collectDataFromDB(){
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


}

fun syncAllChanges(transactionsViewModel: TransactionsViewModel,
                   allMemberInfo: State<List<MemberDetails>>,
                   context: Context){
    transactionsViewModel.syncAllTransactions(allMemberInfo, context)

}

