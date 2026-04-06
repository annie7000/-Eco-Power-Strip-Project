package com.example.powersync

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.ComponentActivity
import com.google.firebase.database.*

class BudgetActivity : ComponentActivity() {

    // UI Elements
    private lateinit var editBudgetInput: EditText
    private lateinit var txtCurrentlyUsing: TextView
    private lateinit var txtShouldUse: TextView
    private lateinit var txtTotalMonth: TextView
    private lateinit var backButton: ImageButton

    // Firebase and Data
    private lateinit var database: DatabaseReference
    private var currentUsageListener: ValueEventListener? = null
    private val electricityRate = 0.15 // $0.15 per kWh
    private var currentKwhUsed = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.budget_planning)

        // 1. Initialize UI Views
        editBudgetInput = findViewById(R.id.edit_budget_input)
        txtCurrentlyUsing = findViewById(R.id.txt_currently_using)
        txtShouldUse = findViewById(R.id.txt_should_use)
        txtTotalMonth = findViewById(R.id.txt_total_month)
        backButton = findViewById(R.id.back_button)

        // 2. Setup Firebase Reference
        database = FirebaseDatabase.getInstance().getReference("power_strip_1")

        // 3. Back Button Logic
        backButton.setOnClickListener {
            finish() // Closes this activity and goes back to the previous one
        }

        // 4. Listen for User Typing the Budget
        editBudgetInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateBudget(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 5. Real-time Firebase Listener
        listenForPowerStripData()
    }

    private fun listenForPowerStripData() {
        currentUsageListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Double::class.java)
                if (value != null) {
                    currentKwhUsed = value
                    updateUI()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BudgetActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }
        database.child("current_usage").addValueEventListener(currentUsageListener as ValueEventListener)
    }

    private fun updateUI() {
        val currentCost = currentKwhUsed * electricityRate

        // Update "You are using" and "Total"
        txtCurrentlyUsing.text = "• You are using: $${String.format("%.2f", currentCost)}"
        txtTotalMonth.text = "$${String.format("%.2f", currentCost)}"

        // Trigger calculation if a budget is already typed in
        calculateBudget(editBudgetInput.text.toString())
    }

    private fun calculateBudget(budgetStr: String) {
        if (budgetStr.isBlank()) {
            txtShouldUse.text = "• You should use ____________"
            txtShouldUse.setTextColor(Color.WHITE)
            return
        }

        val userBudget = budgetStr.toDoubleOrNull()
        if (userBudget == null) {
            txtShouldUse.text = "• Enter a valid budget"
            txtShouldUse.setTextColor(Color.RED)
            return
        }

        val currentCost = currentKwhUsed * electricityRate
        val remaining = userBudget - currentCost

        if (remaining >= 0) {
            txtShouldUse.text = "• You should use: $${String.format("%.2f", remaining)}"
            txtShouldUse.setTextColor(Color.WHITE) // Matches your screenshot's white text
        } else {
            txtShouldUse.text = "• Over Budget!"
            txtShouldUse.setTextColor(Color.RED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentUsageListener?.let { listener ->
            database.child("current_usage").removeEventListener(listener)
        }
        currentUsageListener = null
    }
}
