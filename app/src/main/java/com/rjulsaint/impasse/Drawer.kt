package com.rjulsaint.impasse

import androidx.compose.foundation.background
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rjulsaint.impasse.ui.theme.Purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Drawer {
    @Composable
    fun AppDrawer(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState, navHostController: NavHostController) {
        val navigationItems = listOf(
            NavDrawerItem("Home", ScreenNavigation.Login.route, Icons.Rounded.Home),
            NavDrawerItem("User", ScreenNavigation.NewUser.route, Icons.Rounded.Face),
            NavDrawerItem("Add Password", ScreenNavigation.AddPassword.route, Icons.Rounded.Add),
            NavDrawerItem("Password Vault", ScreenNavigation.ViewPasswords.route, Icons.Rounded.Lock)
        )
        Column(
            modifier = Modifier
                .background(color = Color.LightGray)
        ) {
            navigationItems.forEach { item ->
                val backStackEntry = navHostController.currentBackStackEntryAsState()
                val selected = item.route == backStackEntry.value?.destination?.route

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable(
                            enabled = true,
                            role = Role.Button,
                        ) {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            navHostController.navigate(item.route)
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
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Developed by Ralph Julsaint",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}
