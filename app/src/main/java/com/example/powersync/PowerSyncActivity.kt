package com.example.powersync

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class PowerSyncActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.powersync_page)

        // 1. Find the buttons/views
        val backBtn = findViewById<ImageButton>(R.id.back_button)
        val logoutBtn = findViewById<Button>(R.id.logout_button)
        val devicesBtn = findViewById<View>(R.id.btn_devices_connected)
        val budgetBtn = findViewById<View>(R.id.btn_budget_planning)

        // 2. Back Button: Goes back to Login
        backBtn.setOnClickListener {
            finish()
        }

        logoutBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }

        // 3. Open Devices Connected Page
        devicesBtn.setOnClickListener {
            val intent = Intent(this, ConnectedActivity::class.java)
            startActivity(intent)
        }

        // 4. Open Budget Planning Page
        budgetBtn.setOnClickListener {
            val intent = Intent(this, BudgetActivity::class.java)
            startActivity(intent)
        }
    }
}
