package com.derich.bigfoot.ui.screens.loans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoansViewModel (
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {
    var loans = MutableStateFlow<List<Loan>>(mutableListOf())

    init {
        viewModelScope.launch {
            firebaseDataSource.getAllLoans().collect { loansDetails ->
                updateUI(loansDetails)
            }
        }
    }
    private fun updateUI(loansDetails: List<Loan>) {
        loans.value = loansDetails
    }
}