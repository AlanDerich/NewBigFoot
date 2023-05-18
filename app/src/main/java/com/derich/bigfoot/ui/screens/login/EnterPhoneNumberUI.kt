package com.derich.bigfoot.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.derich.bigfoot.R

@Composable
fun EnterPhoneNumberUI(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit)
) {
    var isError by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize().padding(vertical = 56.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Image(painterResource(id = R.drawable.bigfut1),
//            contentDescription = "Bigfoot Icon",
//            modifier = Modifier
//                .padding(8.dp)
//                .clip(MaterialTheme.shapes.small)
//                .size(104.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.phone_number_text))

        Spacer(modifier = Modifier.height(20.dp))
        PhoneNumberTextField(
            phone = phone,
            onNumberChange = onPhoneChange,
            onDone = onDone,
            isError = isError
        )
        Button(
            onClick = {
                      if (phone.isEmpty()){
                          isError = true
                      }
                else{
                          onClick()
                      }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.next))
        }

    }
}


@Composable
fun PhoneNumberTextField(
    phone: String,
    onNumberChange: (String) -> Unit,
    onDone: (KeyboardActionScope.() -> Unit),
    isError: Boolean
) {
    if (isError) {
        Text(
            text = "Phone Cannot be empty",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 16.dp, top = 0.dp)
        )
    }
    OutlinedTextField(
        value = phone,
        onValueChange = onNumberChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            if (phone.isNotEmpty()){
                onDone()
            }
        }),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Phone, contentDescription = "")
        },
        placeholder = { Text(text = "e.g +254712345678") },
        isError = isError
        )
}
