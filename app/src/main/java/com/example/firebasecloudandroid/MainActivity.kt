package com.example.firebasecloudandroid

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.firebasecloudandroid.ui.theme.FirebaseCloudAndroidTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializar Firebase
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        // ✅ Pedir permiso de notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }

        setContent {
            FirebaseCloudAndroidTheme {
                var tokenText by remember { mutableStateOf("Obteniendo token...") }
                val context = LocalContext.current

                // ✅ Obtener y copiar token
                LaunchedEffect(true) {
                    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                        tokenText = if (task.isSuccessful) {
                            val token = task.result
                            Log.d("FCM", "Token manual: $token")

                            // ✅ Copiar token al portapapeles
                            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("FCM Token", token)
                            clipboard.setPrimaryClip(clip)

                            Toast.makeText(context, "Token copiado al portapapeles", Toast.LENGTH_LONG).show()

                            token
                        } else {
                            "Error al obtener token"
                        }
                    }
                }

                // ✅ Mostrar token en pantalla
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = tokenText,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
