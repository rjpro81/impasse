package com.rjulsaint.impasse

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

class BiometricLoginManager : FragmentActivity(){
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    @Composable
    fun DisplayBiometricPrompt(): Int {
        val context = this//LocalContext.current as FragmentActivity
        var authenticationResult by remember{ mutableStateOf( -1)}
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(context, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    authenticationResult = 1
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric login for Impasse")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        DisplayFingerprint()
        return authenticationResult
    }

    @Composable
    fun DisplayFingerprint(){
        // Prompt appears when user clicks "Log in".
        IconButton(
            onClick = {
                biometricPrompt.authenticate(promptInfo)
            }
        ){
            Image(painter = painterResource(id = R.drawable.baseline_fingerprint_24), contentDescription = "Fingerprint reader icon")
        }
    }
}