package com.example.soberapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
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
import java.time.LocalDate
import java.time.Period
import kotlin.time.measureTime


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
    val yearsMonthsWeeksDays = daysToYearsMonthsWeeksDays(num.value)
    Text(
        text = "Time Sober:\n",
        textAlign = Center,
        modifier = modifier,
        fontSize = 56.sp
    )
    Text(
        text = yearsMonthsWeeksDays,
        textAlign = Center,
        modifier = modifier,
        fontSize = 28.sp
    )
}

@Composable
fun PlusButton(num: MutableState<Int>, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(96.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 40.dp),
                onClick = {
                    num.value++
                }
            )
    ){
        Icon(
            painter = painterResource(R.drawable.plus),
            contentDescription = "Plus Button",
            tint = Color.Unspecified,
            modifier = modifier.size(96.dp)
        )
    }
}

fun daysToYearsMonthsWeeksDays(days: Int): String {
    val startDate = LocalDate.of(0, 1, 1)
    val endDate = startDate.plusDays(days.toLong())
    val period = Period.between(startDate, endDate)
    val years = period.years
    val months = period.months
    val remainingDaysAfterMonths = period.days
    val weeks = remainingDaysAfterMonths / 7
    val daysLeft = remainingDaysAfterMonths % 7
    if (years == 0) {
        if (months == 0) {
            if (weeks == 0) {
                return "$days days"
            }
            else {
                return "$weeks weeks, $daysLeft days"
            }
        }
        else {
            return "$months months, $weeks weeks, $daysLeft days"
        }
    }
    else {
        return "$years years, $months months, $weeks weeks, $daysLeft days"
    }

}