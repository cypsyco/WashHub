package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val buttonWasher = findViewById<Button>(R.id.button_washer)
        val buttonDryer = findViewById<Button>(R.id.button_dryer)

        buttonWasher.setOnClickListener {
            Toast.makeText(this, "Washer clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SelectActivity, WashersActivity::class.java)
            startActivity(intent)
        }

        buttonDryer.setOnClickListener {
            Toast.makeText(this, "Dryer clicked", Toast.LENGTH_SHORT).show()
        }
    }
}