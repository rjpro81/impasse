package com.rjulsaint.impasse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
import com.rjulsaint.impasse.ui.theme.Purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PasswordManagement {
    @Composable
    private fun DisplayPasswordManagementFields(
        navHostController: NavHostController,
        databaseHelper: DatabaseHelper,
        coroutineScope: CoroutineScope,
        scaffoldState: ScaffoldState
    ) {
        val focusManager = LocalFocusManager.current
        val readableDB = databaseHelper.readableDatabase
        //val writeableDB = databaseHelper.writableDatabase
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val passwords = databaseHelper.getAllUserStoredPasswords(
                    readableDB,
                    LoginActivity().sessionMasterPassword
                )
                passwords.forEach { item ->
                    val backStackEntry = navHostController.currentBackStackEntryAsState()
                    val selected = item == backStackEntry
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                coroutineScope.launch {
                                    scaffoldState.drawerState.close()
                                }

                            },
                        backgroundColor = if (selected) Purple500 else Color.White,
                        elevation = 0.dp,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = item[0],
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = item[1],
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = item[2],
                            )
                        }
                    }
                }
            }
        }
    }
    @Composable
    fun DisplayPasswordManagementScreen(
        navHostController: NavHostController,
        databaseHelper: DatabaseHelper
    ) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            val navController = rememberNavController()
            Scaffold(
                topBar = {
                    AppBar().TopBar(
                        coroutineScope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                },
                scaffoldState = scaffoldState,
                drawerBackgroundColor = Color.DarkGray,
                drawerGesturesEnabled = true,
                drawerContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 8.dp, top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(126.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {

                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Drawer().AppDrawer(
                            coroutineScope = coroutineScope,
                            scaffoldState = scaffoldState,
                            navController = navController
                        )
                    }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplayPasswordManagementFields(
                        navHostController = navHostController,
                        databaseHelper = databaseHelper,
                        coroutineScope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }
}