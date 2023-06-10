package com.derich.bigfoot.ui.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.common.composables.CircularProgressBar
import kotlinx.coroutines.ExperimentalCoroutinesApi


//this is the default home screen
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeComposable(modifier: Modifier = Modifier,
                   viewModel: ContributionsViewModel,
                   specificMemberDetails: MemberDetails?,
                   allMemberInfo: State<List<MemberDetails>>
) {
    val context = LocalContext.current
    if(specificMemberDetails != null){
        val allMembersInfo = allMemberInfo.value
//        val memberCont = contributions!!.contains("", )
        Column(modifier = modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val differenceInContributions = viewModel.calculateContributionsDifference(
                    specificMemberDetails.totalAmount.toInt())
                if( differenceInContributions < 0){
                    Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Status of Contribution",
                        modifier = Modifier.size(68.dp))
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. Congrats! You are KSH ${-differenceInContributions} ahead on schedule")

                    }
                else{
                    Icon(painter = painterResource(id = R.drawable.baseline_cancel_24),
                        contentDescription = "Status of Contribution",
                        modifier = Modifier.size(68.dp))
                    Text(text = "Hello ${specificMemberDetails.firstName}, you\'re on ${specificMemberDetails.contributionsDate}. You need KSH $differenceInContributions to be back on track.")
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
            Text(text = "The group's total contributions so far amount to ${calculateTotalContributions(allMemberInfo)}",
                fontWeight = Bold,modifier= Modifier.padding(8.dp), fontSize = 24.sp)
            LazyColumn(modifier = Modifier.padding(top= 8.dp)) {
                items(
                    items = allMembersInfo
                    ) { contribution ->
                    ContributionCard(contribution = contribution,
                        modifier = modifier)
                    }
                }
            }
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressBar(
                isDisplayed = allMembersInfo.isEmpty()
            )

        }
        BackHandler {
            val activity = (context as? Activity)
            activity?.finish()
        }
    }
    else {
        Text(text = "Oops. No details we're not found in our database. Please Contact Admin to make things right.")
    }
}

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
