package com.example.soberapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soberapp.ui.theme.SoberAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var num = remember { mutableIntStateOf(0) }
            SoberAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                            content = {
                                Counter(num)
                                PlusButton(num)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Counter(num: MutableState<Int>, modifier: Modifier = Modifier) {
    Text(
        text = "Time Sober:\n\n" + num.value.toString(),
        textAlign = Center,
        modifier = modifier,
        fontSize = 56.sp
    )
}

@Composable
fun PlusButton(num: MutableState<Int>, modifier: Modifier = Modifier) {
    IconButton(
        onClick = {
            num.value++
        },
        modifier = modifier.size(96.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.plus),
            contentDescription = "Plus Button",
            tint = Color.Unspecified,
            modifier = modifier.size(96.dp)
        )
    }
}