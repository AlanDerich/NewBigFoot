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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavigator
import com.derich.bigfoot.ui.bottomnavigation.NavigationGraph
import com.derich.bigfoot.ui.bottomnavigation.getMemberData
import com.derich.bigfoot.ui.bottomnavigation.memberDetails
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
import com.derich.bigfoot.ui.common.composables.CommonLinearProgressBar
import com.derich.bigfoot.ui.screens.home.ContributionsViewModel
import com.derich.bigfoot.ui.screens.loans.LoansViewModel
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.screens.login.LoginErrorUi
import com.derich.bigfoot.ui.screens.login.PhoneAuthActivity
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.derich.bigfoot.ui.theme.BigFootTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        FirebaseApp.initializeApp(/*context=*/this)
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseDataSource = FirebaseDataSource(firestore)
        val auth = FirebaseAuth.getInstance()
        //viewModels initialization this starts the data collection from the firestore database
        val contributionsVM = ContributionsViewModel(firebaseDataSource)
        val transactionsVM = TransactionsViewModel(firebaseDataSource)
        val loansVM= LoansViewModel(firebaseDataSource)
        val authVM = AuthViewModel(auth)
        var memberDets:MemberDetails? = null
        setContent {
            var notifyLoadingDate by remember { mutableStateOf(false) }
            deviceWidthSize = calculateWindowSizeClass(this).widthSizeClass
            allMemberInformation = contributionsVM.members.collectAsState()
            allTransactions= transactionsVM.transactions.collectAsState()
            allLoans = loansVM.loans.collectAsState()
            if(auth.currentUser != null) {
                notifyLoadingDate=true
                LaunchedEffect(key1 = rememberCoroutineScope()){
                    lifecycleScope.launch(Dispatchers.IO) {
                        contributionsVM.collectDataFromDB()
                    }.invokeOnCompletion {
                        if(contributionsVM.members.value.isNotEmpty()){
                            lifecycleScope.launch(Dispatchers.IO) {
                                memberDets = getMemberData(allMemberInformation.value)
                            }.invokeOnCompletion {
                                notifyLoadingDate = false
                            }
                        }
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        transactionsVM.collectTransactionsFromDB()
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        loansVM.collectLoansFromDB()
                    }
                }
            }
            else {
                val intent = Intent(mainActivity, PhoneAuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }


//                var count = allLoans.value.size
                //navcontroller for the bottom navigation
            val bottomNavController = rememberNavController()
            BigFootTheme {
                if (allMemberInformation.value.isEmpty()) {
                    if (notifyLoadingDate) {
                        CommonLinearProgressBar()
                    }
                }
                else{
                    if(!notifyLoadingDate){
                        if (memberDets== null) {
                            LoginErrorUi(message = "User not found. Contact system administrator to get you setup") {
                                authVM.logOut(context = this)
                            }
                        }
                        else{
                            memberDetails = memberDets!!
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
                    else{
                        CommonLinearProgressBar()
                    }
                }
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