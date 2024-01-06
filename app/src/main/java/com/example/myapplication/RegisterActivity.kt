package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var etUsername: EditText
    private lateinit var etID: EditText
    private lateinit var etPassword: EditText
    private lateinit var sGender: SwitchCompat
    private lateinit var sDormitory: Spinner // 기숙사 정보를 위한 EditText 추가

    var gender = "남자"
    var dorm = "사랑관"

    val maleDorms = arrayOf("사랑관", "소망관")
    val femaleDorms = arrayOf("아름관", "나래관")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageView = findViewById(R.id.imageView)
        etUsername = findViewById(R.id.editTextName)
        etID = findViewById(R.id.editTextID)
        etPassword = findViewById(R.id.editTextPassword)
        sGender = findViewById(R.id.switchGender)
        sDormitory = findViewById(R.id.spinnerDorm)


        imageView.setOnClickListener {
            openGalleryForImage()
        }

        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            registerUser()
            Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        var dropDownRes = emptyArray<String>()
        sGender.setOnCheckedChangeListener{CompoundButton, onSwitch ->
            if (onSwitch){
                gender = "남자"

                val newAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, maleDorms)
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sDormitory.adapter = newAdapter
            }else{
                gender = "여자"

                val newAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, femaleDorms)
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sDormitory.adapter = newAdapter
            }
            Log.d("gender",gender)
        }


        sDormitory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                dorm = selectedItem
                // 선택된 아이템으로 원하는 작업을 수행합니다.
                Log.d("Selected item", selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때 수행할 작업을 여기에 구현할 수 있습니다.
            }
        }

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            val imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun registerUser() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val dormitory = dorm

        RetrofitClient.instance.registerUser(User(username, password, dormitory))
            .enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.message == true) {
                        Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}