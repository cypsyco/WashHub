package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectActivity : AppCompatActivity() {

    private var userid: String? = null
    private var password: String? = null
    private var username: String? = null
    private var dormitory: String? = null
    private var gender: String? = null
    private var image: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        userid = intent.getStringExtra("userid")

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

        //사용자 정보 불러오기
        userid?.let {
            RetrofitClient.instance.getUserDetails(it)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            response.body()?.let { user ->
                                this@SelectActivity.userid = user.userid
                                this@SelectActivity.password = user.pw
                                this@SelectActivity.username = user.username
                                this@SelectActivity.dormitory = user.dormitory
                                this@SelectActivity.gender = user.gender
                                this@SelectActivity.image = user.image
                                Toast.makeText(this@SelectActivity, userid, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@SelectActivity, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@SelectActivity, "에러: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("SelectActivity", "에러: ${t.message}")
                    }

                })
        } ?: run {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
        }
    }
}