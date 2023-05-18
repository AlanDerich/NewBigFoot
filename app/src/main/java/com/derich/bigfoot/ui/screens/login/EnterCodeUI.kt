package com.derich.bigfoot.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.filled.AccountBox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.derich.bigfoot.R

@Composable
fun EnterCodeUI(
    code: String,
    onCodeChange: (String) -> Unit,
    phone: String,
    onGo: (KeyboardActionScope.() -> Unit),
    onNext: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 56.dp,
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = phone,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Waiting to automatically detect an SMS sent to $phone"
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isError) {
            Text(
                text = "Code Cannot be empty",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp))
        }
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(onGo = {
                if (code.isEmpty()){
                    isError= true
                }
                else{
                    onGo()
                }
            }),
            singleLine = true,
            label = { Text(text = stringResource(R.string.code)) },
            leadingIcon = {
                Icon(Icons.Default.AccountBox, contentDescription = "")
            },
            modifier = Modifier.fillMaxWidth(0.45f),
            isError = isError
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                      if (code.isEmpty()){
                          isError = true
                      }
                else{
                          onNext()
                      }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.verify_button))
        }
    }

}

@Preview
@Composable
fun Preview() {
    EnterCodeUI(code = "1234", onCodeChange = {}, phone = "0712345678", onGo = {}, onNext = {})
}