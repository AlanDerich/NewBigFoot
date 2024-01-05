package com.derich.bigfoot

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.derich.bigfoot.model.firebase.FirebaseDataSource
import com.derich.bigfoot.ui.bottomnavigation.BottomNavigator
import com.derich.bigfoot.ui.bottomnavigation.NavigationGraph
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
import com.derich.bigfoot.ui.screens.home.ContributionsViewModel
import com.derich.bigfoot.ui.screens.loans.LoansViewModel
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.derich.bigfoot.ui.theme.BigFootTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {

    companion object {
        var mainActivity: MainActivity? = null

    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivity = this
        super.onCreate(savedInstanceState)
        setContent {
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
                var allMemberInfo = contributionsVM.members.collectAsState()
                var allTransactions= transactionsVM.transactions.collectAsState()
                var allLoans = loansVM.loans.collectAsState()
                var count = allLoans.value.size
                //navcontroller for the bottom navigation
                val bottomNavController = rememberNavController()
                BigFootTheme {
                    Scaffold(
                        topBar = {
                            BigFutAppBar()
                        },
                        bottomBar = {
                            BottomNavigator(navController = bottomNavController, allMemberInfo=allMemberInfo)
                        }
                    ) {
                            innerPadding ->
                        NavigationGraph(
                            navController = bottomNavController,
                            contViewModel = contributionsVM,
                            transactionsViewModel = transactionsVM,
                            authVm= authVM,
                            loansVm = loansVM,
                            allMemberInfo= allMemberInfo,
                            allTransactions = allTransactions,
                            allLoans= allLoans,
                            modifier = Modifier.padding(innerPadding)
                        )

                    }
                }

            }
            else {
                val intent = Intent(mainActivity, LoginActivity::class.java)
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