package com.derich.bigfoot.ui.screens.transactions

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.ui.common.composables.CommonLinearProgressBar
import com.derich.bigfoot.ui.common.composables.CommonVariables.calculateResultingDate
import com.derich.bigfoot.ui.common.composables.CommonVariables.showMessage
import com.derich.bigfoot.ui.common.composables.CommonVariables.toIntOrZero
import com.derich.bigfoot.ui.screens.home.ContributionsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date

var isDisplayed = false

@Composable
fun AddTransactionScreen(
    transactionsViewModel: TransactionsViewModel,
    modifier: Modifier = Modifier,
    contViewModel: ContributionsViewModel,
    navController: NavController
) {
//    var requestToOpen by remember { mutableStateOf(false) }
    Column(modifier = modifier
        .padding(8.dp)
        .verticalScroll(rememberScrollState())) {
        val allMemberInfo = contViewModel.members.collectAsState()
        var selectedMember by remember { mutableStateOf(allMemberInfo.value[0]) }
        val isOpen = rememberSaveable { mutableStateOf(false) } // initial value
        val openCloseOfDropDownList: (Boolean) -> Unit = {
            isOpen.value = it
        }
        val userSelectedString: (MemberDetails) -> Unit = {
            selectedMember = it
        }
        Box {
            Column {
                OutlinedTextField(
                    value = selectedMember.fullNames,
                    onValueChange = { selectedMember.fullNames = it },
                    label = { Text(text = "TextFieldTitle") },
                    modifier = Modifier.wrapContentWidth()
                )
                DropDownList(
                    requestToOpen = isOpen.value,
                    list = allMemberInfo.value,
                    openCloseOfDropDownList,
                    userSelectedString
                )
            }
            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .padding(10.dp)
                    .clickable(
                        onClick = { isOpen.value = true }
                    )
            )
        }

        AddTransactionPage(selectedMember, transactionsViewModel, navController)
    }

}

@Composable
fun AddTransactionPage(selectedMember: MemberDetails,
                       transactionsViewModel: TransactionsViewModel,
                       navController: NavController) {
    var dateOfTransaction by rememberSaveable { mutableStateOf("2020/01/01") }
    var reversedDateOfTransaction by rememberSaveable { mutableStateOf("01/01/2020") }
    var transactionPaidBy by rememberSaveable { mutableStateOf("") }
    var transactionAmountPaid by rememberSaveable { mutableIntStateOf(0) }
    var transactionConfirmation by rememberSaveable { mutableStateOf("") }
    var buttonEnabled by rememberSaveable { mutableStateOf(true) }
    // Fetching the Local Context
    val mContext = LocalContext.current
    OutlinedButton(onClick = {
        val yearSelected: Int
        val monthSelected: Int
        val daySelected: Int
        // Initializing a Calendar
        val mCalendar = Calendar.getInstance()
        // Fetching current year, month and day
        yearSelected = mCalendar.get(Calendar.YEAR)
        monthSelected = mCalendar.get(Calendar.MONTH)
        daySelected = mCalendar.get(Calendar.DAY_OF_MONTH)
        mCalendar.time = Date()
        DatePickerDialog(
            mContext,
            { _: DatePicker, mYear, mMonth, mDayOfMonth ->
                val month = String.format("%02d", mMonth+1)
                val date = String.format("%02d", mDayOfMonth)
                dateOfTransaction = "$mYear/$month/$date"
                reversedDateOfTransaction = "$date/${month}/$mYear"
            }, yearSelected, monthSelected, daySelected
        ).show()
    }) {
        Text(text = dateOfTransaction)

    }
    OutlinedTextField(
        label = { Text(text = "Paid by") },
        value = transactionPaidBy,
        onValueChange = {transactionPaidBy = it},
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionPaidBy == ""))
    OutlinedTextField(value = transactionAmountPaid.toString(),
        onValueChange = { transactionAmountPaid = it.toIntOrZero() },
        label = { Text(text = "Amount paid") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionAmountPaid == 0)
        )
    OutlinedTextField(
        label = { Text(text = "Confirmation Message") },
        value = transactionConfirmation,
        onValueChange = {transactionConfirmation = it},
        modifier = Modifier.padding(top = 4.dp),
        isError = (transactionConfirmation == ""))
    OutlinedTextField(
        label = { Text(text = "Previous amount") },
        value = selectedMember.totalAmount,
        enabled = false,
        onValueChange = {},
        modifier = Modifier.padding(top = 4.dp))
    Button(
        enabled = buttonEnabled,
        onClick = {
            if (dateOfTransaction != "2020/01/01"
                && transactionConfirmation  != ""
                && transactionAmountPaid != 0
                && transactionPaidBy != "")
            {
                buttonEnabled = false
                isDisplayed = true
                //start upload process
                runBlocking {
                    val task = transactionsViewModel.addTransaction(
                        transactionDetails = Transactions(
                            transactionDate = dateOfTransaction,
                            depositFor = selectedMember.fullNames,
                            depositBy = transactionPaidBy,
                            transactionAmount = transactionAmountPaid,
                            transactionConfirmation = transactionConfirmation,
                            savedBy = Firebase.auth.currentUser!!.phoneNumber.toString()
                        ))
                    task.addOnSuccessListener {
                //call this function to update member details if the upload of transaction was successful
                        updateContributions(
                            selectedMember.phoneNumber,
                            selectedMember.fullNames,
                            calculateResultingDate(selectedMember.totalAmount.toInt() + transactionAmountPaid),
                            newUserAmount = (selectedMember.totalAmount.toInt() + transactionAmountPaid).toString(),
                            transactionsViewModel,
                            mContext,
                            navController = navController)
                    }
                    task.addOnFailureListener{
                //do this if the upload failed due to some reason
                        mContext.showMessage("An error occurred during upload ${it.message.toString()}!")
                    }
                }
            }
                else {
                    mContext.showMessage("Please confirm the details again")
                }

                      },
            modifier = Modifier.padding(top = 4.dp))
        {
            Text(text = "Add Transaction")
        }
    if (isDisplayed){
        CommonLinearProgressBar()
    }
}
fun updateContributions(memberPhoneNumber: String,
                        memberFullNames: String,
                        resultingDate: String,
                        newUserAmount: String,
                        transactionsViewModel: TransactionsViewModel,
                        mContext: Context,
                        navController: NavController) {
    runBlocking {
        val task = transactionsViewModel.updateContributions(memberPhoneNumber,memberFullNames,resultingDate, newUserAmount)
        task.addOnSuccessListener {
            isDisplayed= false
            //do this if the upload was successful
            mContext.showMessage("The transaction was updated successfully!")
            transactionsViewModel.launchTransactionScreen(navController = navController)


        }
        task.addOnFailureListener{
            isDisplayed= false
            //do this if the upload failed due to some reason
            mContext.showMessage("An error occurred during upload ${it.message.toString()}!")
        }
    }
}
@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<MemberDetails>,
    request: (Boolean) -> Unit,
    selectedString: (MemberDetails) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.fillMaxWidth(),
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    request(false)
                    selectedString(it)
                },
                text = {
                    Text(it.fullNames, modifier = Modifier.wrapContentWidth())
                }
            )
        }
    }
}
