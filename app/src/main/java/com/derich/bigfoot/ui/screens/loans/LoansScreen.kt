package com.derich.bigfoot.ui.screens.loans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.derich.bigfoot.allLoans
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LoansComposable(modifier: Modifier = Modifier,
                    loansViewModel: LoansViewModel
) {
    //this screen contains details on loans history
//get data from firebase firestone
    var totalOutstandingLoanAmount = 0
    var totalOutstandingLoans = 0
    val loans = allLoans.value
    loans.let {
        it.forEach {loanAmount ->
            if (!loanAmount.status){
                totalOutstandingLoanAmount = totalOutstandingLoanAmount.plus(loanAmount.amountLoaned)
                totalOutstandingLoans++
            }
        }
        Column(modifier = modifier) {
            Text(text = "There are $totalOutstandingLoans unpaid loans.", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.headlineSmall)
            Text(text = "The total amount of unpaid loan is KSH$totalOutstandingLoanAmount.", modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp), style = MaterialTheme.typography.bodyLarge)
            LazyColumn{
                items(
                    items = loans
                ){ loan ->
                    LoansCard( loan = loan,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp))
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressBar(
            isDisplayed = loans.isEmpty()
        )

    }
}

@Composable
fun LoansCard(loan: Loan,
              modifier: Modifier
) {
    Column(horizontalAlignment = Alignment.Start,
        modifier = modifier
            .border(border = BorderStroke(width = 2.dp, color = Color.White))
            .padding(8.dp)
            .fillMaxWidth()) {
        Text(text = loan.username,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Date Loaned: ${ loan.dateLoaned }",
            modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Amount Loaned: KSH ${ loan.amountLoaned }",
            modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        Spacer(modifier = Modifier.padding(2.dp))
        Text(text = "Transaction Charges: KSH ${ loan.transactionCharges }",
            modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        Spacer(modifier = Modifier.padding(2.dp))
        if (loan.status){
            Text(text = "Status: Repaid",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = Color.Green,
                fontWeight = FontWeight.Bold)
            Text(text = "Repaid Date: ${loan.dateRepaid}",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Text(text = "Repaid Amount: ${loan.amountRepaid}",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
        }
        else {
            Text(text = "Status: Not Paid",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                color = Color.Red,
                fontWeight = FontWeight.Bold)
        }
    }
}
