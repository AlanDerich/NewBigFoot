package com.derich.bigfoot.ui.screens.loans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.LoanType
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoansViewModel (
    private val firebaseDataSource: FirebaseDataSource
) : ViewModel() {
    var loans = MutableStateFlow<List<Loan>>(mutableListOf())
    private var listOfAllLoans= mutableListOf<Loan>()
    private var listOfAllTopUps= mutableListOf<Loan>()
    private var listOfAllExpenses= mutableListOf<Loan>()
    var allTransCharges = 0
    var allPendingLoans = 0
    var allPaidLoans = 0
    var allPaidLoansTotal =0
    var allGroupLoans = 0
    var allGroupLoansTotal = 0
    var allOutstandingLoansTotalAmount = 0
    var allRechargesTotal = 0
    var allExpensesTotal = 0
    var availableAmount =0
    var totalProfits =0
    init {
        viewModelScope.launch {
            firebaseDataSource.getAllLoans().collect { loansDetails ->
                if (loansDetails.size!=loans.value.size){
                    updateUI(loansDetails)
                }
            }
        }
    }
    private fun updateUI(loansDetails: List<Loan>) {
        loans.value = loansDetails
    }
    //function to check all outstanding loans and total
    fun differentiateTheLoansTransactions(){
        for (loan in loans.value){
            when (loan.type) {
                LoanType.PERSONAL_LOAN -> {
                    listOfAllLoans.add(loan)
                }
                LoanType.EXPENSE -> {
                    listOfAllExpenses.add(loan)
                }
                else -> {
                    listOfAllTopUps.add(loan)
                }
            }
        }
        getAllLoansInfo()
    }
    private fun getAllLoansInfo(){
        allOutstandingLoansTotalAmount= getAllOutstandingLoansTotal()
        allRechargesTotal = getAllRecharges()
        allExpensesTotal = getAllExpenses()
        availableAmount = (allRechargesTotal+ allPaidLoansTotal) - (allExpensesTotal + allGroupLoansTotal + allTransCharges)
        val totalProfitsBeforeTransactionCharges = allGroupLoansTotal - allPaidLoansTotal - allExpensesTotal
//        totalProfits = totalProfitsBeforeTransactionCharges - allTransCharges
        //get a better function (outstanding+availableamount) - initial amount
        totalProfits = (allOutstandingLoansTotalAmount + availableAmount) - (allRechargesTotal)


    }
    //get all transaction charges and total pending loans
    private fun getAllOutstandingLoansTotal(): Int{
        var outstandingAmount = 0
        for (loan in listOfAllLoans){
            if (!loan.status){
                outstandingAmount += loan.amountLoaned
                allPendingLoans ++
            }
            if (loan.status){
                allPaidLoansTotal += loan.amountRepaid!!
                allPaidLoans ++
            }
            allTransCharges+= loan.transactionCharges
            allGroupLoansTotal +=loan.amountLoaned
        }
        allGroupLoans = listOfAllLoans.size
        return outstandingAmount
    }
    //get all top-ups from the main account
    private fun getAllRecharges(): Int{
        var allRecharges = 0
        for (loan in listOfAllTopUps){
            allRecharges+=loan.amountLoaned
        }
        return allRecharges
    }
    //get all expenses
    private fun getAllExpenses(): Int{
        var allExpenses=0
        for (loan in listOfAllExpenses){
            allExpenses+= loan.amountLoaned
        }
        return allExpenses
    }

    fun clearData() {
        allTransCharges = 0
        allPendingLoans = 0
        allPaidLoans = 0
        allPaidLoansTotal =0
        allGroupLoans = 0
        allGroupLoansTotal = 0
        allOutstandingLoansTotalAmount = 0
        allRechargesTotal = 0
        allExpensesTotal = 0
        availableAmount =0
        totalProfits= 0
        listOfAllLoans.clear()
        listOfAllTopUps.clear()
        listOfAllExpenses.clear()
    }
}