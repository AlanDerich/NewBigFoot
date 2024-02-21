package com.derich.bigfoot.ui.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import com.derich.bigfoot.deviceWidthSize
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.bottomnavigation.memberDetails
import com.derich.bigfoot.ui.screens.transactions.TransactionsViewModel


//this is the default home screen
@Composable
fun HomeComposable(modifier: Modifier = Modifier,
                   transactionsViewModel: TransactionsViewModel
) {
    val context = LocalContext.current

    //call this function only if I(admin) am logged in to sync all changes
    if (memberDetails.fullNames == "Alan Gitonga Wanjiru"){
        syncAllChanges(transactionsViewModel, allMemberInformation, context)
    }
    //get the current list of member details from the flow state
    val allMembersInfo = allMemberInformation.value
//        val memberCont = contributions!!.contains("", )
    //check device window size is portrait and display the portrait version
    if (deviceWidthSize == WindowWidthSizeClass.Compact){
        CompactScreen(modifier = modifier, allMembersInfo = allMembersInfo)
    }
    //if the device orientation is landscape
    else{
        LandscapeScreen(modifier,allMembersInfo)
    }


    BackHandler {
        val activity = (context as? Activity)
        activity?.finish()
    }
}
//calculate the total member contributions to display on the home screen
fun calculateTotalContributions(allMemberInfo: State<List<MemberDetails>>): Int {
    var totalAmount =0
    allMemberInfo.value.forEach {
        totalAmount+=it.totalAmount.toInt()
    }
    return totalAmount
}
//a model for member details
@Composable
fun ContributionCard(contribution: MemberDetails,
                     modifier: Modifier
) {
    Card(modifier=modifier,
        elevation = CardDefaults.cardElevation()) {
        Row(
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
@Composable
fun CompactScreen(modifier: Modifier, allMembersInfo: List<MemberDetails>){
    Column(modifier = modifier.fillMaxSize()) {
        val differenceInContributions = calculateContributionsDifference(
            memberDetails.totalAmount.toInt())
        Row(horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {
            if( differenceInContributions > 0){
                Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
                    contentDescription = "Status of Contribution is current",
                    modifier = Modifier
                        .size(68.dp)
                        .weight(0.5f))
                Column(horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "KSH $differenceInContributions",
                        fontWeight = Bold,
                        modifier= Modifier.padding(2.dp))
                    Text(text = memberDetails.contributionsDate,
                        fontWeight = Bold,
                        modifier= Modifier.padding(2.dp))

                }
            }
            else{
                Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
                    contentDescription = "Status of Contribution",
                    modifier = Modifier
                        .size(68.dp)
                        .weight(0.5f))
                Column (modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "KSH $differenceInContributions",
                        fontWeight = Bold,
                        modifier= Modifier.padding(2.dp))
                    Text(text = memberDetails.contributionsDate,
                        fontWeight = Bold,
                        modifier= Modifier.padding(2.dp))

                }
            }
            Column (modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "Group Total", fontWeight = Bold)
                Text(text = "KSH ${calculateTotalContributions(allMemberInformation)}",
                    fontWeight = Bold,modifier= Modifier.padding(2.dp), fontSize = 16.sp)

            }
        }
        //list here
        LazyColumn(modifier = Modifier
            .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                items = allMembersInfo
            ) { contribution ->
                ContributionCard(contribution = contribution,
                    modifier = modifier.fillMaxWidth()
                        .padding(top = 8.dp))
            }
        }
    }
}
@Composable
fun LandscapeScreen(modifier: Modifier, allMembersInfo: List<MemberDetails>) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)) {
        val differenceInContributions = calculateContributionsDifference(
            memberDetails.totalAmount.toInt())
        Row(horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top) {
            if( differenceInContributions > 0){
                Column(horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.wrapContentWidth()) {
//                        Spacer(modifier = Modifier.padding(2.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Status of Contribution",
                        modifier = Modifier
                            .size(68.dp))
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Your Total KSH $differenceInContributions",
                        fontWeight = Bold)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Due: ${memberDetails.contributionsDate}",
                        fontWeight = Bold)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Group Total: KSH ${calculateTotalContributions(allMemberInformation)}",
                        fontWeight = Bold,fontSize = 16.sp)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        ) {
                    items(allMembersInfo) { contribution ->
                        ContributionCard(contribution = contribution,
                            modifier = modifier)
                    }
                }

            }
            else{
                Column(horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.wrapContentWidth()) {
                    Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = "Status of Contribution not unto date",
                        modifier = Modifier
                            .size(68.dp)
                           )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "Your Total KSH $differenceInContributions",
                        fontWeight = Bold)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Due: ${memberDetails.contributionsDate}",
                        fontWeight = Bold)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Group Total: KSH ${calculateTotalContributions(allMemberInformation)}",
                        fontWeight = Bold,fontSize = 16.sp)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        ) {
                    items(allMembersInfo) { contribution ->
                        ContributionCard(contribution = contribution,
                            modifier = modifier)
                    }
                }
            }
        }
    }
}



