package com.rjulsaint.impasse

//import android.content.res.Resources.NotFoundException
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.rjulsaint.impasse.ui.theme.ImPasseTheme
import kotlinx.coroutines.CoroutineScope


class ViewPasswordsActivity(
    val navHostController: NavHostController,
    val databaseHelper: DatabaseHelper,
    val sessionManager: SessionManager,
    val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState
) {
    private val tag : String = "ViewPasswordActivity"
    @Composable
    private fun DisplayViewPasswordFields() {
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val focusManager = LocalFocusManager.current
        var openDialogWindow by remember { mutableStateOf(false) }
        var category by remember { mutableStateOf("") }
        var passUserName by remember { mutableStateOf("") }
        var passPassword by remember { mutableStateOf("") }
        var changeCardColor by remember { mutableStateOf(false) }
        var changeCardFontColor by remember { mutableStateOf(false) }
        var isPasswordDialogEditable = false


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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                )
                var passwordsList : MutableList<List<String>>? = null
                try {
                    passwordsList = databaseHelper.getAllUserStoredPasswords(
                        databaseHelper.writeableDB,
                        sessionManager.sessionUserName!!,
                        sessionManager.sessionMasterPassword!!
                    )
                } catch(ex : Exception){
                    Log.e(tag, "Unable to access database to retrieve a list of all passwords.", ex)
                }

                var cardIndex = 0
                passwordsList!!.forEach { password ->
                    changeCardColor = (cardIndex % 2) == 0
                    changeCardFontColor = (cardIndex % 2) == 0
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable(enabled = true, onClickLabel = "View Password", onClick =  {
                                isPasswordDialogEditable = false
                                category = password[0]
                                passUserName = password[1]
                                passPassword = password[2]
                                openDialogWindow = true
                            }),
                            elevation = 5.dp,
                        backgroundColor = if(changeCardColor) Color.Cyan else Color.Magenta
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 24.dp),
                                    text = password[0],
                                    fontSize = 20.sp,
                                    color = if(changeCardFontColor) Color.Black else Color.White
                                )

                                IconButton(onClick = {
                                    isPasswordDialogEditable = true
                                    category = password[0]
                                    passUserName = password[1]
                                    passPassword = password[2]
                                    openDialogWindow = true
                                    }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_edit_24),
                                        "Edit Password",
                                        Modifier.size(30.dp)
                                    )
                                }
                            }


                            if(openDialogWindow){
                                ViewPasswordDialogActivity().DisplayViewPasswordDialogBox(
                                    onDismiss = { openDialogWindow = false },
                                    category,
                                    passUserName,
                                    passPassword,
                                    databaseHelper,
                                    sessionManager,
                                    isPasswordDialogEditable
                                )
                            }

                            Text(
                                modifier = Modifier
                                    .padding(start = 24.dp),
                                text = password[1],
                                fontSize = 20.sp,
                                color = if((cardIndex % 2) == 0) Color.Black else Color.White
                            )
                            val passwordVisible by remember { mutableStateOf(false) }
                            val openDialog = remember { mutableStateOf(false) }
                            val dismissAlertDialog = remember { mutableStateOf(true) }

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    modifier = Modifier
                                        .padding(start = 24.dp),
                                    text = if (passwordVisible) password[2] else "**********",
                                    softWrap = true,
                                    overflow = TextOverflow.Visible,
                                    fontSize = 20.sp,
                                    color = if((cardIndex % 2) == 0) Color.Black else Color.White
                                )
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

                                                        //var numOfRecordsDeleted: Int = -1

                                                        try {
                                                           /* numOfRecordsDeleted =
                                                                databaseHelper.deletePassword(
                                                                    databaseHelper.writeableDB,
                                                                    webAddress!!,
                                                                    description!!
                                                                )*/
                                                        } catch (ex: Exception) {
                                                            Log.e(
                                                                tag,
                                                                "Unable to access database to delete password",
                                                                ex
                                                            )
                                                        }

                                                        /*if (numOfRecordsDeleted > 0) {
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
                                                        }*/
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
                    cardIndex++
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @Composable
    fun ViewPasswordsScreen() {
        ImPasseTheme {
            val coroutineScope = rememberCoroutineScope()
            val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
            Scaffold(
                topBar = { AppBar().TopBar(
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState,
                    navHostController = navHostController,
                    sessionManager = sessionManager,
                    databaseHelper = databaseHelper,
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
                        Text(text = sessionManager.sessionUserName!!, color = Color.Magenta)

                        Drawer().AppDrawer(coroutineScope = coroutineScope, scaffoldState = scaffoldState, navHostController = navHostController)
                    }
                }
            ) { contentPadding ->
                Box(modifier = Modifier.padding(contentPadding)) {
                    DisplayViewPasswordFields()
                }
            }
        }
    }
}
