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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rjulsaint.impasse.ui.theme.Purple500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Drawer {
    @Composable
    fun AppDrawer(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {
        val navigationItems = listOf(
            NavDrawerItem("Home", "home", Icons.Rounded.Home),
            NavDrawerItem("User", "user", Icons.Rounded.Face),
            NavDrawerItem("Add Password", "add password", Icons.Rounded.Add),
            NavDrawerItem("Password Vault", "vault", Icons.Rounded.Lock)
        )
        Column(
            modifier = Modifier
                .background(color = Color.LightGray)
        ) {
            navigationItems.forEach { item ->
                val backStackEntry = navController.currentBackStackEntryAsState()
                val selected = item.route == backStackEntry.value?.destination?.route
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                            navController.navigate(item.route)
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