package com.derich.bigfoot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavigator
import com.derich.bigfoot.ui.bottomnavigation.NavigationGraph
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
import com.derich.bigfoot.ui.common.composables.CommonLinearProgressBar
import com.derich.bigfoot.ui.screens.home.ContributionsViewModel
import com.derich.bigfoot.ui.screens.loans.LoansViewModel
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.screens.login.PhoneAuthActivity
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.derich.bigfoot.ui.theme.BigFootTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


lateinit var allMemberInformation: State<List<MemberDetails>>
lateinit var allTransactions: State<List<Transactions>>
lateinit var allLoans: State<List<Loan>>
var deviceWidthSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact
//var collectingMembersData=false

class MainActivity : ComponentActivity() {
    companion object {
        var mainActivity: MainActivity? = null
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this
        super.onCreate(savedInstanceState)
        setContent {
            deviceWidthSize = calculateWindowSizeClass(this).widthSizeClass
            FirebaseApp.initializeApp(/*context=*/this)
            val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            val firebaseDataSource = FirebaseDataSource(firestore)
            val auth = FirebaseAuth.getInstance()
//            val authServImp = AuthImplementation(LoginActivity(), auth)
            val authVM = AuthViewModel(auth)
            //check if user is logged in or not
            if(auth.currentUser != null) {
                //viewmodel handling all actions on contributions transactions and loans
                val contributionsVM = ContributionsViewModel(firebaseDataSource)
                contributionsVM.collectDataFromDB()
                val transactionsVM = TransactionsViewModel(firebaseDataSource)
                transactionsVM.collectTransactionsFromDB()
                val loansVM= LoansViewModel(firebaseDataSource)
                loansVM.collectLoansFromDB()
//                LaunchedEffect(Unit) { contributionsVM.members }
                //login viewmodel handling all login activities
//                val allMemberInfo = contributionsVM.members.collectAsState()
                //get all data from the Firestore database using flow to automatically update the data in case of changes
                allMemberInformation = contributionsVM.members.collectAsState()
                allTransactions= transactionsVM.transactions.collectAsState()
                allLoans = loansVM.loans.collectAsState()
//                var count = allLoans.value.size
                //navcontroller for the bottom navigation
                val bottomNavController = rememberNavController()
                var dataLoading by remember { mutableStateOf(true) }
                BigFootTheme {
                    if (allMemberInformation.value.isEmpty()) {
                        if (dataLoading) {
                            CommonLinearProgressBar()

                        }
                    }
                    else{
                        dataLoading = false
                        Scaffold(
                            topBar = {
                                BigFutAppBar()
                            },
                            bottomBar = {
                                BottomNavigator(navController = bottomNavController)
                            },
                        ) {
                                innerPadding ->
                            NavigationGraph(
                                navController = bottomNavController,
                                contViewModel = contributionsVM,
                                transactionsViewModel = transactionsVM,
                                authVm= authVM,
                                loansVm = loansVM,
                                modifier = Modifier.padding(innerPadding)
                            )

                        }
                    }
                }

            }
            else {
                val intent = Intent(mainActivity, PhoneAuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        mainActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity = null
    }
}

@Preview
@Composable
fun MainPrev(){

}
