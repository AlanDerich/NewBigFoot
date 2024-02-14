package com.derich.bigfoot.ui.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.derich.bigfoot.R
import com.derich.bigfoot.allMemberInformation
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel


//this is the default home screen
@Composable
fun HomeComposable(modifier: Modifier = Modifier,
                   transactionsViewModel: TransactionsViewModel,
                   specificMemberDetails: MemberDetails?
) {
    val context = LocalContext.current

    //call this function only if I am logged in to sync all changes
    if(specificMemberDetails != null){
        if (specificMemberDetails.fullNames == "Alan Gitonga Wanjiru"){
            syncAllChanges(transactionsViewModel, allMemberInformation, context)
        }


        val allMembersInfo = allMemberInformation.value
//        val memberCont = contributions!!.contains("", )
        Column(modifier = modifier.fillMaxSize()) {
            val differenceInContributions = calculateContributionsDifference(
                specificMemberDetails.totalAmount.toInt())
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                if( differenceInContributions > 0){
                    Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Status of Contribution",
                        modifier = Modifier.size(68.dp)
                            .weight(0.5f))
                    Column(horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(text = "KSH $differenceInContributions",
                            fontWeight = Bold,
                            modifier= Modifier.padding(2.dp))
                        Text(text = specificMemberDetails.contributionsDate,
                            fontWeight = Bold,
                            modifier= Modifier.padding(2.dp))

                    }
                }
                else{
                    Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = "Status of Contribution",
                        modifier = Modifier.size(68.dp)
                            .weight(0.5f))
                    Column (modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(text = "KSH $differenceInContributions",
                            fontWeight = Bold,
                            modifier= Modifier.padding(2.dp))
                        Text(text = specificMemberDetails.contributionsDate,
                            fontWeight = Bold,
                            modifier= Modifier.padding(2.dp))

                    }
                }
                Column (modifier = Modifier
                    .weight(1f).padding(end = 8.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Group Total", fontWeight = Bold)
                    Text(text = "KSH ${calculateTotalContributions(allMemberInformation)}",
                        fontWeight = Bold,modifier= Modifier.padding(2.dp), fontSize = 16.sp)

                }
            }
            //row here
            LazyColumn(modifier = Modifier.padding(top= 8.dp).fillMaxWidth()) {
                items(
                    items = allMembersInfo
                ) { contribution ->
                    ContributionCard(contribution = contribution,
                        modifier = modifier)
                }
            }
        }
//        Column(modifier = modifier.padding(8.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                val differenceInContributions = viewModel.calculateContributionsDifference(
//                    specificMemberDetails.totalAmount.toInt())
//                if( differenceInContributions < 0){
//                    Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
//                        contentDescription = "Status of Contribution",
//                        modifier = Modifier.size(68.dp))
//                    Column {
//                        Spacer(modifier = Modifier.padding(2.dp))
////                    Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. Congrats! You are KSH ${-differenceInContributions} ahead on schedule")
//                        Text(text = "KSH ${-differenceInContributions}")
//                        Text(text = specificMemberDetails.contributionsDate)
//
//                    }
//
//
//                    }
//                else{
//                    Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
//                        contentDescription = "Status of Contribution",
//                        modifier = Modifier.size(68.dp))
////                    Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. You need KSH $differenceInContributions to be back on track.")
//                    Text(text = specificMemberDetails.contributionsDate)
//                    Text(text = "${-differenceInContributions}")
//                }
//                Spacer(modifier = Modifier.size(8.dp))
//            }
////            Text(text = "The group's total contributions so far amount to ${calculateTotalContributions(allMemberInfo)}",
////                fontWeight = Bold,modifier= Modifier.padding(8.dp), fontSize = 24.sp)
//            }
        BackHandler {
            val activity = (context as? Activity)
            activity?.finish()
        }
    }
    else {
        Text(text = "Oops. No details we're not found in our database. Please Contact Admin to make things right.")
    }
}

//function to generate pdf
//fun ConvertToPdf(activity: Activity, context: Context, allMemberInfo: List<MemberDetails>) {
//
//    /*
//    * var usersList: ArrayList<User> = ArrayList()
//    usersList.add(User(name = "Mostafa",age =  15 ,  country = "Egypt"))
//    usersList.add(User(name = "Ahmed", age = 25 ,  country = "Egypt"))
//    usersList.add(User(name = "Mohammed",age =  35 ,  country = "Egypt"))
//    usersList.add(User(name = "Kareem", age = 40,  country = "Egypt"))
//*
//    * */
//
//    if (ContextCompat.checkSelfPermission(context,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        == PackageManager.PERMISSION_GRANTED) {
//        PdfExtractor().Builder()
//            .setDocsName("Gad")
//            .setDocumentTitle("Gad Title")
//            .setHeaders(allMemberInfo)
//            .setDocumentContent(allMemberInfo)
//            .build(context)
//    } else {
//        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        requestPermissions(
//            context as Activity, permission,
//            212)
//    }
//
//}
fun calculateTotalContributions(allMemberInfo: State<List<MemberDetails>>): Int {
    var totalAmount =0
    allMemberInfo.value.forEach {
        totalAmount+=it.totalAmount.toInt()
    }
    return totalAmount
}

@Composable
fun ContributionCard(contribution: MemberDetails,
                     modifier: Modifier
) {
    Row(
        modifier = modifier.padding(start = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(contribution.profPicUrl),
            contentDescription = stringResource(R.string.profile_image_description),
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .size(68.dp)
        )
        UsersColumn(contribution = contribution)
    }
}

@Composable 
fun UsersColumn(modifier: Modifier = Modifier, contribution: MemberDetails) {
        Column(horizontalAlignment = Alignment.Start, modifier = modifier.padding(8.dp)) {
            Text(text = contribution.fullNames, fontWeight = Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = "KSH ${contribution.totalAmount}")
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = contribution.contributionsDate)
        } 
}
