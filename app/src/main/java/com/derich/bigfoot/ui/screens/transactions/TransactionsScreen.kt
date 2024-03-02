package com.derich.bigfoot.ui.screens.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.derich.bigfoot.deviceWidthSize
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.ui.bottomnavigation.memberDetails
import com.derich.bigfoot.ui.common.composables.CommonLinearProgressBar
import com.derich.bigfoot.ui.common.composables.CommonVariables
import com.derich.bigfoot.ui.common.composables.MemberRole
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalAnimationApi::class)
@Composable
fun TransactionsComposable(modifier: Modifier = Modifier,
                           transactionsViewModel: TransactionsViewModel,
                           navController: NavController
) {
    //variable to hold the visibility state of the animation
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
//    val context = LocalContext.current
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val transactions = filterTransactionsList(textState, allTransactions.value)
    //check if the device is in compact mode
    if (deviceWidthSize == WindowWidthSizeClass.Compact){
        Column {
            SearchView(textState, modifier = Modifier.fillMaxWidth())
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Column {
                    AnimatedVisibility(
                        visibleState = visibleState,
                        enter = fadeIn(
                            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                        ), exit = fadeOut()
                    ) {
                        LazyColumn {
                            itemsIndexed(transactions) { index, transaction ->
                                TransactionCard(
                                    transaction = transaction,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                )
                            }
                        }
                    }
                }

//display all the transactions as a horizontal list
                //check if member is admin and display button to launch addTransaction page
                if (memberDetails.memberRole == MemberRole.ADMIN.name) {
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
    }
    //display the landscape version
    else{
        Column {
            Row{
                SearchView(textState, modifier = Modifier.weight(0.25f))
                Box(
                    modifier = modifier.weight(0.75f),
                    contentAlignment = Alignment.BottomEnd
                ) {
//display all the transactions as a horizontal list
                    LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
                        items(transactions
                        ) { transaction ->
                            TransactionCard(
                                transaction = transaction,
                                modifier = modifier.padding(top = 8.dp, start = 8.dp)
                            )
                        }
                    }
                    //check if member is admin and display button to launch addTransaction page
                    if (memberDetails.memberRole == MemberRole.ADMIN.name) {
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
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (transactions.isEmpty()){
            CommonLinearProgressBar()
        }

    }
}
//model of the transaction
@Composable
fun TransactionCard(transaction: Transactions,
                    modifier: Modifier
) {
    Card(modifier=modifier,
        elevation = CardDefaults.cardElevation()) {
        Column(horizontalAlignment = Alignment.Start,
            modifier = modifier
//                .border(border = BorderStroke(width = 2.dp, color = Color.White))
                .padding(8.dp)
                .fillMaxWidth()) {
            Text(text = transaction.depositFor,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Date: ${ transaction.transactionDate }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "Amount: ${CommonVariables.Currency} ${ transaction.transactionAmount }",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp))
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = transaction.transactionConfirmation,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>, modifier: Modifier) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = modifier
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
