package com.example.soberapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.soberapp.ui.theme.SoberAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val DAYS_KEY = intPreferencesKey("days")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var numState by remember { mutableStateOf(0)}
            LaunchedEffect(Unit) {
                numState = getDays(dataStore).first()
            }
            SoberAppTheme {
                SoberAppScreen(
                    num = numState,
                    onIncrement = {
                        numState++
                        lifecycleScope.launch {
                            saveDays(dataStore, numState)
                        }
                    },
                    resetDays = {
                        numState = 0
                        lifecycleScope.launch {
                            saveDays(dataStore, numState)
                        }
                    })
            }
        }
    }
}

suspend fun saveDays(dataStore: DataStore<Preferences>, days: Int) {
    dataStore.edit { preferences ->
        preferences[DAYS_KEY] = days
    }
}

fun getDays(dataStore: DataStore<Preferences>): Flow<Int> {
    return dataStore.data.map { preferences ->
        preferences[DAYS_KEY] ?: 0
    }
}

@Composable
fun SoberAppScreen(num: Int, resetDays: () -> Unit, onIncrement: () -> Unit) {
    var settingsVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        innerPadding ->
        TopMenuBar(
            settingsVisible = settingsVisible,
            setSettingsVisible = { settingsVisible = it }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Counter(num)
                PlusButton(onIncrement)
            }
        }
        if (settingsVisible) {
            SettingsMenu(resetDays)
        }
    }
}

@Composable
fun Counter(num: Int) {
    val yearsMonthsWeeksDays = daysToYearsMonthsWeeksDays(num)
    Text(
        text = "Time Sober:\n",
        textAlign = TextAlign.Center,
        fontSize = 56.sp
    )
    Text(
        text = yearsMonthsWeeksDays,
        textAlign = TextAlign.Center,
        fontSize = 28.sp
    )
}

@Composable
fun PlusButton(onIncrement: () -> Unit) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 40.dp),
                onClick = onIncrement
            )
    ) {
        Icon(
            painter = painterResource(if (isSystemInDarkTheme()) R.drawable.plus_dark else R.drawable.plus),
            contentDescription = "Plus Button",
            tint = Color.Unspecified,
            modifier = Modifier.size(96.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenuBar(settingsVisible: Boolean, setSettingsVisible: (Boolean) -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Sober App",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {setSettingsVisible(!settingsVisible)}) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings Icon"
                        )
                    }
                }
            )
        }
    ) {innerPadding ->

    }
}

@Composable
fun SettingsMenu(resetDays : () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .width(225.dp)
                .height(100.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(16.dp))
        ) {
            Button(
                onClick = { resetDays() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
                modifier = Modifier
                    .background(Color.Transparent)
                    .width(175.dp)
                    .height(50.dp)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = "Reset Days Sober",
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer)

                )
            }
        }
    }

}



fun daysToYearsMonthsWeeksDays(days: Int): String {
    val startDate = LocalDate.of(0, 1, 1)
    val endDate = startDate.plusDays(days.toLong())
    val period = Period.between(startDate, endDate)
    val years = period.years
    val months = period.months
    val remainingDays = period.days
    val weeks = remainingDays / 7
    val daysLeft = remainingDays % 7

    return buildString {
        if (years > 0) append("$years years, ")
        if (months > 0) append("$months months, ")
        if (weeks > 0) append("$weeks weeks, ")
        append("$daysLeft days")
    }.trimEnd(',', ' ')
}
