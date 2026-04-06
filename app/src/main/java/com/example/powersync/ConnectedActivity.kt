package com.example.powersync

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class ConnectedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_connected) // Matches device_connected.xml

        // 1. Initialize the views from your XML
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val deviceNameDisplay = findViewById<TextView>(R.id.device_name)
        val turnOffButton = findViewById<Button>(R.id.turn_off_button) // Ensure this ID is in your XML

        // 2. Set the device name dynamically
        // Later, this could come from Firebase or an Intent!
        deviceNameDisplay.text = "Refrigerator"

        // 3. Back Button logic: Return to the Power Sync hub
        backButton.setOnClickListener {
            finish()
        }

        // 4. "Turn Off" logic
        turnOffButton.setOnClickListener {
            // Here you would send a command to your power strip/Firebase
            Toast.makeText(this, "${deviceNameDisplay.text} turned off", Toast.LENGTH_SHORT).show()

            val shouldTurnOn = turnOffButton.text.toString().equals("Turn Off", ignoreCase = true)
            turnOffButton.text = if (shouldTurnOn) "Turn On" else "Turn Off"
        }
    }
}
