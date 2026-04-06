package com.example.powersync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    companion object {
        private const val AUTH_PREFS = "auth_prefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameInput = findViewById<EditText>(R.id.username_input)
        val passwordInput = findViewById<EditText>(R.id.username_pass)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        val authPrefs = getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)

        usernameInput.setText(authPrefs.getString(KEY_USERNAME, ""))

        signInButton.setOnClickListener {
            val user = usernameInput.text.toString().trim()
            val pass = passwordInput.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authPrefs.edit()
                .putString(KEY_USERNAME, user)
                .putString(KEY_PASSWORD, pass)
                .apply()

            Toast.makeText(this, "Sign in details saved", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            val user = usernameInput.text.toString().trim()
            val pass = passwordInput.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedUsername = authPrefs.getString(KEY_USERNAME, null)
            val savedPassword = authPrefs.getString(KEY_PASSWORD, null)

            if (savedUsername == null || savedPassword == null) {
                Toast.makeText(this, "Please sign in first to save credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (user != savedUsername || pass != savedPassword) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, PowerSyncActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}
