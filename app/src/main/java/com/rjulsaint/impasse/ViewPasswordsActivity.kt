package com.rjulsaint.impasse

import android.content.res.Resources.NotFoundException
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
import kotlin.math.roundToInt


class ViewPasswordsActivity {
    private val tag : String = "ViewPasswordActivity"
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun DisplayViewPasswordFields(databaseHelper: DatabaseHelper, navHostController: NavHostController){
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val focusManager = LocalFocusManager.current
        val swipeableState = rememberSwipeableState(0)
        var webAddress: String? = null
        var description: String? = null

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            val columnWidth = 320.dp
            val widthPx = with(LocalDensity.current){
                (columnWidth - 30.dp).toPx()
            }
            val anchors = mapOf(0f to 0, widthPx to 1)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .swipeable(
                        state = swipeableState,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal
                    )
            ) {
                Text("Passwords", color = Color.Gray)
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                var passwordsList : MutableList<List<String>>? = null
                try {
                    passwordsList = databaseHelper.getAllUserStoredPasswords(
                        databaseHelper.writeableDB,
                        LoginActivity().sessionUser,
                        LoginActivity().sessionMasterPassword
                    )
                } catch(ex : Exception){
                    Log.e(tag, "Unable to access database to retrieve a list of all passwords.", ex)
                }

                passwordsList!!.forEach { password ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                            .background(Color.DarkGray),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color.LightGray
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = password[0]
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = password[1]
                            )
                            var passwordVisible by remember { mutableStateOf(false) }
                            val openDialog = remember { mutableStateOf(false) }
                            val dismissAlertDialog = remember { mutableStateOf(true) }

                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = if (passwordVisible) password[2] else "**********",
                                softWrap = true,
                                overflow = TextOverflow.Visible
                            )
                            val image = if (passwordVisible) {
                                painterResource(id = R.drawable.baseline_visibility_24)
                            } else {
                                painterResource(id = R.drawable.baseline_visibility_off_24)
                            }
                            val passwordContentDescription =
                                if (passwordVisible) "Hide password" else "Show password"

                            Row {
                                // Toggle button to hide or display password
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        image,
                                        passwordContentDescription,
                                        Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                                IconButton(onClick = {
                                    clipboardManager.setText(
                                        AnnotatedString(password[2])
                                    ); Toast.makeText(
                                    context,
                                    "Copied to clipboard",
                                    Toast.LENGTH_SHORT
                                ).show()
                                }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.outline_content_copy_24),
                                        "Copy Contents",
                                        Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.padding(start = 25.dp, end = 25.dp))
                                IconButton(onClick = {
                                    webAddress = password[0]
                                    description = password[1]
                                    openDialog.value = true
                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_delete_24),
                                        "Delete Password",
                                        Modifier.size(30.dp)
                                    )
                                }
                                if (openDialog.value) {
                                    AlertDialog(
                                        onDismissRequest = { dismissAlertDialog.value },
                                        title = {
                                            Text(text = "Alert!!")
                                        },
                                        text = {
                                            Text(text = "Are you sure you want to delete password?")
                                        },
                                        buttons = {
                                            Row(
                                                modifier = Modifier
                                                    .padding(all = 8.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Button(
                                                    onClick = {
                                                        openDialog.value = false

                                                        var numOfRecordsDeleted: Int = -1

                                                        try {
                                                            numOfRecordsDeleted =
                                                                databaseHelper.deletePassword(
                                                                    databaseHelper.writeableDB,
                                                                    webAddress!!,
                                                                    description!!
                                                                )
                                                        } catch (ex: Exception) {
                                                            Log.e(
                                                                tag,
                                                                "Unable to access database to delete password",
                                                                ex
                                                            )
                                                        }

                                                        if (numOfRecordsDeleted > 0) {
                                                            try {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Password deleted",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                navHostController.navigate(
                                                                    ScreenNavigation.ViewPasswords.route
                                                                )
                                                            } catch (ex: IllegalArgumentException) {
                                                                Log.e(
                                                                    tag,
                                                                    "Unable to navigate screens due to invalid route given",
                                                                    ex
                                                                )
                                                            } catch (ex: NotFoundException) {
                                                                Log.e(
                                                                    tag,
                                                                    "Unable to locate resource for displaying toast.",
                                                                    ex
                                                                )
                                                            }
                                                        }
                                                    }
                                                ) {
                                                    Text("Yes")
                                                }
                                                Spacer(
                                                    modifier = Modifier.padding(
                                                        start = 5.dp,
                                                        end = 5.dp
                                                    )
                                                )
                                                Button(
                                                    onClick = {
                                                        openDialog.value = false
                                                    }
                                                ) {
                                                    Text("No")
                                                }
                                            }
                                        },
                                        properties = DialogProperties(
                                            dismissOnBackPress = true,
                                            dismissOnClickOutside = true,
                                        )
                                    )
                                }
                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    fun ViewPasswordsScreen(navHostController: NavHostController, databaseHelper: DatabaseHelper) {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            Scaffold(
                topBar = { AppBar().TopBar(
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState,
                    databaseHelper = databaseHelper,
                    navHostController = navHostController
                ) },
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
                            Image(
                                modifier = Modifier
                                    .matchParentSize(),
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = "",
                            )
                            Image(
                                modifier = Modifier
                                    .scale(1.4f),
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "",
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Drawer().AppDrawer(coroutineScope = coroutineScope, scaffoldState = scaffoldState, navHostController = navHostController)
                    }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplayViewPasswordFields(databaseHelper, navHostController)
                }
            }
        }
    }
}
