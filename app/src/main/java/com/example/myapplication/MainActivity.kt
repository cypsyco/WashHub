package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var etUserid: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUserid = findViewById(R.id.etUserid)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            loginUser()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val userid = etUserid.text.toString().trim()
        val password = etPassword.text.toString().trim()

        RetrofitClient.instance.loginUser(User(userid, password, "", "", "", ""))
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.message == true) {
                        Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MainActivity, NavigateActivity::class.java)
                        intent.putExtra("userid", userid)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}
