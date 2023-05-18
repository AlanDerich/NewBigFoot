package com.derich.bigfoot.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.derich.bigfoot.R

@Composable
fun ErrorUi(
    exc: Throwable,
    onRestart: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val text = exc.message
        if (text != null) {
            Text(text = text, color = MaterialTheme.colors.error)
        } else {
            Text(text = stringResource(R.string.generic_error))
        }
        Button(onClick = onRestart) {
            Text(text = stringResource(R.string.try_))
        }
    }

}