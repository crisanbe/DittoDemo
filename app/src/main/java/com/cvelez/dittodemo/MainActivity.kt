package com.cvelez.dittodemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cvelez.dittodemo.ui.TaskScreen
import com.cvelez.dittodemo.ui.TaskViewModel
import com.cvelez.dittodemo.ui.theme.DittoDemoTheme
import live.ditto.transports.DittoSyncPermissions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()

        setContent {
            DittoDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskScreen(
                        viewModel = TaskViewModel(application as MyApp),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkPermissions() {
        val missing = DittoSyncPermissions(this).missingPermissions()
        if (missing.isNotEmpty()) {
            this.requestPermissions(missing, 0)
        }
    }
}