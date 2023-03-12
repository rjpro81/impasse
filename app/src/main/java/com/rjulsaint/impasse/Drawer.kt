package com.rjulsaint.impasse

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rjulsaint.impasse.ui.theme.Purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Drawer {
    private val tag : String = "Drawer"
    @Composable
    fun AppDrawer(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
        val navHostController = rememberNavController()
        val navigationItems = listOf(
            NavDrawerItem("Login", ScreenNavigation.Login.route, Icons.Rounded.Home),
            NavDrawerItem("User", ScreenNavigation.NewUser.route, Icons.Rounded.Face),
            NavDrawerItem("Add Password", ScreenNavigation.AddPassword.route, Icons.Rounded.Add),
            NavDrawerItem("Password Vault", ScreenNavigation.ViewPasswords.route, Icons.Rounded.Lock)
        )
        Column {
            navigationItems.forEach { item ->
                val backStackEntry = navHostController.currentBackStackEntryAsState()
                val selected = item.route == backStackEntry.value?.destination?.route

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            try {
                                navHostController.navigate(item.route)
                            } catch (ex: IllegalArgumentException) {
                                Log.e(tag, "Unable to navigate due to invalid route given.", ex)
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
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "${item.name} Icon"
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 24.dp),
                            text = item.name,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
