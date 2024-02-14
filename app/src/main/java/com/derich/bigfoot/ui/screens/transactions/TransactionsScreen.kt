package com.derich.bigfoot.ui.screens.transactions

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.derich.bigfoot.allTransactions
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TransactionsComposable(modifier: Modifier = Modifier,
                           transactionsViewModel: TransactionsViewModel,
                           memberInfo: MemberDetails?,
                           navController: NavController
) {

//    val context = LocalContext.current
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val transactions = filterTransactionsList(textState, allTransactions.value)
//    DropdownMenu(expanded = , onDismissRequest = { /*TODO*/ }) {
//
//    }
    Column {
        SearchView(textState)
        Box(
            modifier = modifier,
            contentAlignment = Alignment.BottomEnd
        ) {
//display all the transactions as a horizontal list
            LazyColumn {
                items(
                    items = transactions
                ) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        modifier = modifier
                    )
                }
            }
            //check if member is admin and display button to launch addTransaction page
            if (memberInfo!!.memberRole == "admin") {
                FloatingActionButton(
                    onClick = {
                        transactionsViewModel.launchAddTransactionScreen(navController)
                    }
                )
                {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Transaction Button")
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
                style = MaterialTheme.typography.headlineSmall,
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

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
//        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.medium, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}





fun filterTransactionsList(state: MutableState<TextFieldValue>, shopsList: List<Transactions>): List<Transactions> {

    val filteredItems: List<Transactions>
    val searchedText = state.value.text
    filteredItems = if (searchedText.isEmpty()) {
        shopsList
    } else {
        val resultList = ArrayList<Transactions>()
        for (item in shopsList) {
            if (item.transactionDate.lowercase(Locale.getDefault())
                    .contains(searchedText.lowercase(Locale.getDefault()))
                ||item.depositFor.lowercase(Locale.getDefault())
                    .contains(searchedText.lowercase(Locale.getDefault()))
                ||item.transactionAmount.toString().lowercase(Locale.getDefault())
                    .contains(searchedText.lowercase(Locale.getDefault()))
                ||item.transactionConfirmation.lowercase(Locale.getDefault())
                    .contains(searchedText.lowercase(Locale.getDefault()))
            ) {
                resultList.add(item)
            }
        }
        resultList
    }
    return filteredItems
}
