package com.derich.bigfoot.ui.screens.transactions

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.derich.bigfoot.R
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TransactionsComposable(modifier: Modifier = Modifier,
                           transactionsViewModel: TransactionsViewModel,
                           memberInfo: MemberDetails?,
                           navController: NavController,
                           allTransactions: State<List<Transactions>>
) {

    val context = LocalContext.current
    val transactions = allTransactions.value
//    DropdownMenu(expanded = , onDismissRequest = { /*TODO*/ }) {
//
//    }
    Box(modifier = modifier,
        contentAlignment = Alignment.BottomEnd) {
//display all the transactions as a horizontal list
        LazyColumn{
            items(
                items = transactions
            ){ transaction ->
                TransactionCard( transaction = transaction,
                    modifier = modifier)
            }
        }
        //check if member is admin and launch addTransaction page
        if (memberInfo!!.phoneNumber == "+254792705723"){
            IconButton(
                onClick = {
                    Toast.makeText(context, "Add Button Clicked", Toast.LENGTH_SHORT).show()
                    transactionsViewModel.launchAddTransactionScreen(navController)
                },
                enabled = true
            )
            {
                Image(
                    painterResource(id = R.drawable.baseline_add),
                    contentDescription = "Add Icon",
                    modifier = Modifier
                        .size(64.dp))
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressBar(
            isDisplayed = transactions.isEmpty()
        )

    }
}

@Composable
fun TransactionCard(transaction: Transactions,
                    modifier: Modifier
) {
        Column(horizontalAlignment = Alignment.Start,
            modifier = modifier
                .border(border = BorderStroke(width = 2.dp, color = Color.White))
                .padding(8.dp)
                .fillMaxWidth()) {
            Text(text = transaction.depositFor,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Date: ${ transaction.transactionDate }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Amount: KSH ${ transaction.transactionAmount }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = transaction.transactionConfirmation,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis)
        }
}
