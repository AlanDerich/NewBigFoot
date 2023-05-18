package com.derich.bigfoot.ui.screens.loans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminAddLoansScreen(modifier: Modifier = Modifier) {
    var loanUsername by remember {mutableStateOf("")}
    var amountLoaned by remember {mutableStateOf("")}
    var dateLoaned by remember {mutableStateOf("")}
    var status by remember {mutableStateOf(false)}
    var transactionCharges by remember {mutableStateOf("")}
    var dateRepaid by remember {mutableStateOf("")}
    var amountRepaid by remember {mutableStateOf("")}
    Column(modifier = modifier) {
        Text(text = "Please enter the details of the loan below:")
        OutlinedTextField(value =loanUsername, onValueChange = {loanUsername = it})
        Spacer(modifier = Modifier.padding(2.dp))
        OutlinedTextField(value =amountLoaned, onValueChange = {amountLoaned = it})

        OutlinedTextField(value =dateLoaned, onValueChange = {dateLoaned = it})
//        OutlinedTextField(value =status, onValueChange = {status = it})
        OutlinedTextField(value =transactionCharges, onValueChange = {transactionCharges = it})
    }
}