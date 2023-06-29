package com.dgomesdev.exchangeapp.presentation.ui.composables

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import com.dgomesdev.exchangeapp.presentation.screens.ConvertCoinsAction
import com.dgomesdev.exchangeapp.presentation.screens.SaveExchangeValues

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeApp(
    convertCoinsAction: ConvertCoinsAction,
    saveExchangeValues: SaveExchangeValues,
    exchangeValues: ExchangeValues?,
    exchangeValue: Double,
    savedValues: List<ExchangeValues>
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = { ExchangeTopBar() },
        bottomBar = { ExchangeBottomBar { navController.navigate(it) } }
    ) {
        ExchangeNavHost(
            navController = navController,
            modifier = Modifier.padding(8.dp),
            convertCoinsAction = convertCoinsAction,
            saveExchangeValues = saveExchangeValues,
            exchangeValues = exchangeValues,
            exchangeValue = exchangeValue,
            savedValues = savedValues
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeTopBar() {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(stringResource(R.string.exchange_app)) },
        actions = {
            IconButton(onClick = {
                Toast.makeText(
                    context,
                    "Developed by Dgomes Dev",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Developed by Dgomes Dev"
                )
            }
        }
    )
}

@Composable
fun ExchangeBottomBar(
    navigateToScreen: (String) -> Unit
) {
    var currentScreen by rememberSaveable {
        mutableStateOf(0)
    }
    val screens = listOf("Main screen", "History screen")
    NavigationBar {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = currentScreen == index,
                onClick = { navigateToScreen(screen) ; currentScreen = index },
                icon = {
                    if (index == 0) Icon(imageVector = Icons.Default.Home, contentDescription = "Main screen")
                    else Icon(painter = painterResource(R.drawable.ic_history), contentDescription = "History")
                       },
                label = {Text(
                    if (index == 0) stringResource(R.string.main_screen)
                    else stringResource(R.string.history)
                )
                }
            )
        }
    }
}