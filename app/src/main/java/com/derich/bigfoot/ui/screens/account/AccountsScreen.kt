package com.derich.bigfoot.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.bottomnavigation.BottomNavItem
import com.derich.bigfoot.ui.screens.login.AuthViewModel
import com.derich.bigfoot.ui.theme.BigFootTheme

@Composable
fun AccountsComposable(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    memberInfo: MemberDetails
) {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()) {
        BadgedBox(badge = {
            Badge {  IconButton(content = { Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = modifier
                    .size(16.dp)
                    .clip(MaterialTheme.shapes.medium))
            },
            onClick = {navController.navigate(BottomNavItem.ImageUploader.screenRoute)}) } }) {
            Image(painter = rememberAsyncImagePainter(memberInfo.profPicUrl),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(140.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

        }

        Text(text = " ${ memberInfo.firstName +" "+ memberInfo.secondName +" "+ memberInfo.surname} ",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(8.dp))
        Text(text = memberInfo.phoneNumber,
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(8.dp))
        Button(onClick = {
            authViewModel.logOut(context = context)

        },
                modifier = Modifier.padding(8.dp)) {
            Text(text = "Log Out",
                style = MaterialTheme.typography.button)
        }
        Button(onClick = {
            authViewModel.deleteAccount(context, navController)
        },
            modifier = Modifier.padding(8.dp)) {
            Text(text = "Delete Account",
                style = MaterialTheme.typography.button)
        }
    }
}

@Composable
fun deleteUser() {

}

@Preview
@Composable
fun DefaultPreview(modifier: Modifier = Modifier) {
    BigFootTheme {
//        AccountsComposable(userProfile = userProfile.user)
    }
}