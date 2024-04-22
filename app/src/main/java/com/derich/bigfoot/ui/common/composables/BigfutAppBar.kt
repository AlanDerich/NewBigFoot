package com.derich.bigfoot.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.derich.bigfoot.R

@Composable
fun BigFutAppBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier.background(colorResource(id = R.color.teal_200)).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.bigfut1),
                contentDescription = "App Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )) {
                    append("B")
                }
                withStyle(style = SpanStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )){
                    append("igFut")
                }
            })
    }
}