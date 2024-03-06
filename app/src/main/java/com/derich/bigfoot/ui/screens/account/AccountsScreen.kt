package com.derich.bigfoot.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.derich.bigfoot.deviceWidthSize
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.derich.bigfoot.ui.common.composables.CommonVariables.CURRENT_USER_DETAILS
import com.derich.bigfoot.ui.common.composables.CommonVariables.MemberRole
import com.derich.bigfoot.ui.screens.login.AuthViewModel

@Composable
fun AccountsComposable(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val memberDetails = CURRENT_USER_DETAILS
    val context = LocalContext.current
    //display portrait screen
    if(deviceWidthSize == WindowWidthSizeClass.Compact) {
        Column(
            modifier = modifier.padding(8.dp).verticalScroll(rememberScrollState()).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
                //make screen scrollable in case it is small
        ) {
            BadgedBox(badge = {
                Badge {
                    IconButton(content = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = modifier
                                .size(16.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    },
                        onClick = { navController.navigate(BottomNavItem.ImageUploader.screenRoute) })
                }
            },modifier=Modifier.padding(4.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(memberDetails.profPicUrl),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

            }

            Text(
                text = " ${memberDetails.firstName + " " + memberDetails.secondName + " " + memberDetails.surname} ",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = memberDetails.phoneNumber,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
            if(memberDetails.memberRole == MemberRole.ADMIN.name){
                //export data button
                Button(
                    onClick = {
                        authViewModel.navigateToExportDataActivity(context = context)

                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Export Data",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            //logout button
            Button(
                onClick = {
                    authViewModel.logOut(context = context)

                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = {
                    authViewModel.deleteAccount(navController)
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Delete Account",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    //display landscape screen
    else{
        Row(modifier = modifier.fillMaxSize()){
            Column(
                modifier = Modifier
                    .padding(8.dp).weight(1f)
                    //make screen scrollable in case it is small
                    .verticalScroll(rememberScrollState())
            ) {
                BadgedBox(badge = {
                    Badge {
                        IconButton(content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .size(16.dp)
//                                    .clip(MaterialTheme.shapes.medium)
                            )
                        },
                            onClick = { navController.navigate(BottomNavItem.ImageUploader.screenRoute) })
                    }
                }) {
                    Image(
                        painter = rememberAsyncImagePainter(memberDetails.profPicUrl),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(140.dp)
//                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            }
            Column(modifier= Modifier.verticalScroll(rememberScrollState()).padding(8.dp).weight(1f)) {
                Text(
                    text = " ${memberDetails.firstName + " " + memberDetails.secondName + " " + memberDetails.surname} ",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = memberDetails.phoneNumber,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Column(modifier= Modifier.verticalScroll(rememberScrollState()).padding(8.dp).weight(1f)) {

                //show export data button if Admin
                if(memberDetails.memberRole == MemberRole.ADMIN.name){
                    Button(
                        onClick = {
                            authViewModel.navigateToExportDataActivity(context = context)
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Export Data",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                //logout button
                Button(
                    onClick = {
                        authViewModel.logOut(context = context)
                    },
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                ) {
                    Text(
                        text = "Log Out",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(
                    onClick = {
                        authViewModel.deleteAccount(navController)
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Delete Account",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

//@Composable
//fun DeleteUser() {
//
//}