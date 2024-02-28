package com.derich.bigfoot.ui.screens.loans

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.derich.bigfoot.R
import com.derich.bigfoot.allLoans
import com.derich.bigfoot.deviceWidthSize
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.LoanType
import com.derich.bigfoot.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun LoansComposable(modifier: Modifier = Modifier,
                    loansViewModel: LoansViewModel
) {
    //this screen contains details on loans history
//get data from firebase firestone
    var showStatsDialog by remember { mutableStateOf(false) }
    var totalOutstandingLoanAmount = 0
    var totalOutstandingLoans = 0
    val loans = allLoans.value
    if (showStatsDialog) {
        DisplayStatsDialog(
            loansViewModel = loansViewModel,
            onDismiss = { showStatsDialog = false
            loansViewModel.clearData()}
        )
    }
    loans.let {
        it.forEach {loanAmount ->
            if (!loanAmount.status){
                totalOutstandingLoanAmount = totalOutstandingLoanAmount.plus(loanAmount.amountLoaned)
                totalOutstandingLoans++
            }
        }
        //device is in portrait mode
        if(deviceWidthSize == WindowWidthSizeClass.Compact){
            Column(modifier = modifier) {
                Text(
                    text = "There are $totalOutstandingLoans unpaid loans.",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "The total amount of unpaid loan is KSH$totalOutstandingLoanAmount.",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { showStatsDialog = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Full Stats")
                }
                LazyColumn {
                    items(
                        items = loans
                    ) { loan ->
                        LoansCard(
                            loan = loan,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
        }
        //display landscape screen
        else{
            Row {
                Column(modifier = modifier
                    .verticalScroll(rememberScrollState()).weight(0.25f)) {
                    Text(
                        text = "There are $totalOutstandingLoans unpaid loans.",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "The total amount of unpaid loan is KSH$totalOutstandingLoanAmount.",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(
                        onClick = { showStatsDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Full Stats")
                    }
                }
                LazyVerticalGrid(columns = GridCells.Adaptive(200.dp), modifier = Modifier.weight(0.75f)) {
                    items(
                        items = loans
                    ) { loan ->
                        LoansCard(
                            loan = loan,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                        )
                    }
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
fun DisplayStatsDialog(loansViewModel: LoansViewModel,
                       onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnBackPress = false)) {
        loansViewModel.differentiateTheLoansTransactions()
        /*
        * display all group top-ups
        * display all total loans given and total
        * display all transaction charges
        * display all loans repaid and total
        * display all remaining loans and total
        * display all expenses
        * display all available cash
        *
        * display profits = amount of money given out- amount repaid...
        * */
        Card(shape = CardDefaults.elevatedShape) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
//                .background(MaterialTheme.colorScheme.onSecondaryContainer)
                .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {
                Image(
                    painterResource(id = R.drawable.bigfut1),
                    contentDescription = "Bigfut logo",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(
                            RoundedCornerShape(8.dp)
                        ))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "All Deposits: KSH ${loansViewModel.allRechargesTotal}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "${loansViewModel.allGroupLoans} total loans: KSH ${loansViewModel.allGroupLoansTotal}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "All transaction charges: KSH ${loansViewModel.allTransCharges}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "${loansViewModel.allPaidLoans} paid loans totalling: KSH ${loansViewModel.allPaidLoansTotal}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "${loansViewModel.allPendingLoans} unpaid loans totalling: KSH ${loansViewModel.allOutstandingLoansTotalAmount}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "All Group Expenses: KSH ${loansViewModel.allExpensesTotal}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Available Amount: KSH ${loansViewModel.availableAmount}",
                    style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Total Profits: KSH ${loansViewModel.totalProfits}",
                    style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Dismiss")
                }
            }
        }
    }
}

@Composable
fun LoansCard(loan: Loan,
              modifier: Modifier
) {
    Card(modifier=modifier,
        elevation = CardDefaults.cardElevation()) {
        Column(horizontalAlignment = Alignment.Start,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()) {
            //check if the transaction is a loan and display the loan info
            if (loan.type == LoanType.PERSONAL_LOAN){
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
            //display this if it is a recharge
            else if (loan.type ==LoanType.RECHARGE){
                Text(text = "Date: ${ loan.dateLoaned }",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "Amount Recharged: KSH ${ loan.amountLoaned }",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            }
            //display this if it is an expense
            else if (loan.type == LoanType.EXPENSE){
                Text(text = "Expense Date: ${ loan.dateLoaned }",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "Amount used: KSH ${ loan.amountLoaned }",
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "Details: ${loan.username}",
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            }
        }
    }

}
