package com.rjulsaint.impasse
/*
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

class AlertDialogManager(private val databaseHelper: DatabaseHelper, private val context: Context) {
    private val tag: String = "AlertDialogManager"

    @Composable
    fun DisplayDialog(currentOpenDialogState: Boolean, currentDismissAlertState: Boolean, currentEventName: String){
        val openDialog = remember { mutableStateOf(currentOpenDialogState) }
        val dismissAlertDialog = remember { mutableStateOf(currentDismissAlertState) }
        val eventName = remember { mutableStateOf(currentEventName) }

        if(openDialog.value) {
            AlertDialog(
                onDismissRequest = { dismissAlertDialog.value },
                title = {
                    Text(text = "Alert!!")
                },
                text = {
                    Text(text = if(eventName.value == "Reset Database") "Are you sure you want to reset database?\nThis will delete all saved application data" else "Are you sure you want to delete all passwords?\nThis operation cannot be undone")
                },
                buttons = {
                    Row(
                        modifier = Modifier
                            .padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                try {
                                    val numOfRecordsDeleted = databaseHelper.deleteAllPasswords(databaseHelper.writeableDB)
                                    if(numOfRecordsDeleted > 0) {
                                        Toast.makeText(
                                            context,
                                            "Passwords deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    openDialog.value = false
                                } catch (ex: Exception) {
                                    Log.e(
                                        tag,
                                        "Unable to access database to delete passwords",
                                        ex
                                    )
                                }
                            }
                        ) {
                            Text("Yes")
                        }
                        Spacer(modifier = Modifier.padding(start = 5.dp, end = 5.dp))
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

 */