package com.derich.bigfoot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavigator
import com.derich.bigfoot.ui.bottomnavigation.NavigationGraph
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
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

class MainActivity : ComponentActivity() {
    companion object {
        var mainActivity: MainActivity? = null

    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
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
            if(FirebaseAuth.getInstance().currentUser != null) {
                //viewmodel handling all actions on contributions
                val contributionsVM = ContributionsViewModel(firebaseDataSource)
                val transactionsVM = TransactionsViewModel(firebaseDataSource)
                val loansVM= LoansViewModel(firebaseDataSource)
//                LaunchedEffect(Unit) { contributionsVM.members }
                //login viewmodel handling all login activities
//                val allMemberInfo = contributionsVM.members.collectAsState()
                allMemberInformation = contributionsVM.members.collectAsState()
                allTransactions= transactionsVM.transactions.collectAsState()
                allLoans = loansVM.loans.collectAsState()
//                var count = allLoans.value.size
                //navcontroller for the bottom navigation
                val bottomNavController = rememberNavController()
                BigFootTheme {
                    Scaffold(
                        topBar = {
                            BigFutAppBar()
                        },
                        bottomBar = {
                            BottomNavigator(navController = bottomNavController)
                        },
                        //adding a FAB to add a deposit to your account
                        floatingActionButton = {
                            Box(){
                                FloatingActionButton(
                                    onClick = { /* stub */ },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(50.dp)
                                        .offset(y = 50.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center,
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