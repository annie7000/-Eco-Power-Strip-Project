package com.example.powersync

// changes made by annie 4/7/2026

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase

class ConnectedActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val db = FirebaseDatabase.getInstance().getReference("commands")

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

        // 4. "ON/OFF" logic
        turnOffButton.setOnClickListener {

            val isTurningOff = turnOffButton.text.toString().equals("Turn Off", true)
            val newState = if (isTurningOff) "OFF" else "ON"

            db.child("relay1").setValue(newState) // send button state over to firebase

            Toast.makeText(this, "Relay set to $newState", Toast.LENGTH_SHORT).show()
            turnOffButton.text = if (isTurningOff) "Turn On" else "Turn Off"
        }
    }
}
