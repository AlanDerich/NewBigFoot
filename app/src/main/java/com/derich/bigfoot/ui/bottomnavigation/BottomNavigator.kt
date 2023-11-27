package com.derich.bigfoot.ui.bottomnavigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.derich.bigfoot.R
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.derich.bigfoot.ui.screens.DataDeletionRequest
import com.derich.bigfoot.ui.screens.ImageUploaderScreen
import com.derich.bigfoot.ui.screens.account.AccountsComposable
import com.derich.bigfoot.ui.screens.home.ContributionsViewModel
import com.derich.bigfoot.ui.screens.home.HomeComposable
import com.derich.bigfoot.ui.screens.loans.LoansComposable
import com.derich.bigfoot.ui.screens.loans.LoansViewModel
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.screens.login.LoginErrorUi
import com.derich.bigfoot.ui.screens.transactions.AddTransactionScreen
import com.derich.bigfoot.ui.screens.transactions.TransactionsComposable
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BottomNavigator(
    navController: NavController,
    allMemberInfo: State<List<MemberDetails>>) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions,
        BottomNavItem.Loans,
        BottomNavItem.Account
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title,
                    fontSize = 9.sp) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                enabled = allMemberInfo.value.isNotEmpty(),
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
@Composable
fun NavigationGraph(
    navController: NavHostController,
    contViewModel: ContributionsViewModel,
    transactionsViewModel: TransactionsViewModel,
    authVm: AuthViewModel,
    loansVm:LoansViewModel,
    allMemberInfo: State<List<MemberDetails>>,
    allTransactions: State<List<Transactions>>,
    allLoans: State<List<Loan>>,
    modifier: Modifier
) {
//    var allMemberInfo by remember { mutableStateOf(List<MemberDetails>) }
    val memberDetails: MemberDetails
    val context = LocalContext.current
    if (allMemberInfo.value.isNotEmpty()) {
        if (getMemberData(allMemberInfo.value) == null) {
            LoginErrorUi(message = "User not found. Contact system administrator to get you setup") {
                authVm.logOut(context)
            }
        }
        else{
        memberDetails = getMemberData(allMemberInfo.value)!!
        NavHost(
            navController,
            startDestination = BottomNavItem.Home.screenRoute,
            modifier = modifier
        ) {
            composable(BottomNavItem.Home.screenRoute) {
                HomeComposable(
                    viewModel = contViewModel,
                    specificMemberDetails = memberDetails,
                    allMemberInfo = allMemberInfo
                )
            }

            composable(BottomNavItem.Transactions.screenRoute) {
                TransactionsComposable(
                    transactionsViewModel = transactionsViewModel,
                    memberInfo = memberDetails,
                    navController = navController,
                    allTransactions = allTransactions
                )
            }
            composable(BottomNavItem.Loans.screenRoute) {
                LoansComposable(loansViewModel = loansVm,
                    memberInfo = memberDetails,
                    allLoans= allLoans)
        }
            composable(BottomNavItem.Account.screenRoute) {
                AccountsComposable(
                    navController= navController,
                    authViewModel = authVm,
                    memberInfo = memberDetails)
        }
            composable(BottomNavItem.AddTransaction.screenRoute) {
                AddTransactionScreen(
                transactionsViewModel = transactionsViewModel,
                contViewModel = contViewModel,
                navController = navController)
        }
            composable(BottomNavItem.DeleteAccount.screenRoute) {
                DataDeletionRequest(
                    authViewModel = authVm
                )
            }
            composable(BottomNavItem.ImageUploader.screenRoute) {
                ImageUploaderScreen(memberDetails, transactionsViewModel)
            }
        }
    }
}

}


fun getMemberData(memberInfo: List<MemberDetails>): MemberDetails? {
    var memberDets: MemberDetails?
    memberInfo.forEach {memberDetails ->
        if (memberDetails.phoneNumber == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            memberDets = memberDetails
            return memberDets
        }
    }
    return null
}