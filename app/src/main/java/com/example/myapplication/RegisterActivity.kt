package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {

    private lateinit var imageView: CircleImageView
    private lateinit var etUsername: EditText
    private lateinit var etID: EditText
    private lateinit var etPassword: EditText
    private lateinit var sGender: Switch
    private lateinit var sDormitory: Spinner
    private lateinit var idCheckBtn: Button
    private var isUserIdChecked = false // ID 중복 확인 상태

    var gender = "남자"
    var dorm = "사랑관"

    val maleDorms = arrayOf("사랑관", "소망관")
    val femaleDorms = arrayOf("아름관", "나래관")

    private var imageuri: Uri? = null

    @SuppressLint("MissingInflatedId", "WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        imageView = findViewById(R.id.imageView)
        etUsername = findViewById(R.id.editTextName)
        etID = findViewById(R.id.editTextID)
        etPassword = findViewById(R.id.editTextPassword)
        sGender = findViewById(R.id.switchGender)
        sDormitory = findViewById(R.id.spinnerDorm)

//        val resourceId: Int = R.drawable.baseline_person_24 // 여기에 자신이 사용하려는 drawable의 리소스 ID를 넣어주세요
//        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, resourceId)
//
//        // 비트맵을 파일로 저장
//        val file = File(this.filesDir, "image.jpg")
//        val outputStream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//        outputStream.flush()
//        outputStream.close()
//
//        imageuri = FileProvider.getUriForFile(this, this.packageName + ".provider", file)

        // toolbar
        val toolbartitle = findViewById<TextView>(R.id.toolBarTitle)
        toolbartitle.text = "회원가입"
        val toolbardorm = findViewById<TextView>(R.id.toolBarDorm)
        toolbardorm.text = ""
        val backbtn = findViewById<ImageButton>(R.id.toolBarBtn)
        backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //password
        val showbtn = findViewById<ImageButton>(R.id.showbutton)
        showbtn.tag = "0"
        showbtn.setOnClickListener {
            Log.d("buttonClicked", showbtn.tag.toString())
            when(it.tag){
                "0" -> {
                    Log.d("buttonClicked1", showbtn.tag.toString())
                    showbtn.tag = "1"
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    showbtn.setImageResource(R.drawable.hide)
                }
                "1" -> {
                    Log.d("buttonClicked1", showbtn.tag.toString())
                    showbtn.tag = "0"
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    showbtn.setImageResource(R.drawable.view)
                }
            }
            etPassword.setSelection(etPassword.text!!.length)
        }

        // profileImage
        imageView.setOnClickListener {
            openGalleryForImage()
        }

        idCheckBtn = findViewById(R.id.idCheckBtn)
        idCheckBtn.setOnClickListener {
            checkUserIdAvailability()
        }

        val newAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, maleDorms)
        newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sDormitory.adapter = newAdapter

        // gender
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

        // dorm
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

        //register
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            registerUser()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // 현재 액티비티를 종료하여 뒤로가기와 동일한 효과
                true
            }
            else -> super.onOptionsItemSelected(item)
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
            imageuri = data?.data!!
            Glide.with(this)
                .load(imageuri)
                .circleCrop()
                .into(imageView)
//            imageView.setImageURI(imageuri)
        }
    }

    private fun registerUser() {
        val userid = etID.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val dormitory = dorm
        val gender = gender
        val image = if(imageuri!=null) {
            encodeuriToBase64(imageuri!!)
        } else{
            null
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.baseline_person_24)
//            val outputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            val imageBytes = outputStream.toByteArray()
//            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        }

        if (userid.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isUserIdChecked) {
            Toast.makeText(this, "ID 중복확인을 진행해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.registerUser(User(userid, password, username, dormitory, gender, image))
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

        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun encodeImageToBase64(imageView: ImageView): String {
        imageView.buildDrawingCache()
        val bitmap = imageView.drawingCache

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()

        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
    private fun encodeuriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun checkUserIdAvailability() {
        val userid = etID.text.toString().trim()
        if (userid.isEmpty()) {
            Toast.makeText(this, "ID를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        val call = RetrofitClient.instance.checkUserId(UserIdCheckRequest(userid))
        call.enqueue(object : Callback<UserIdCheckResponse> {
            override fun onResponse(call: Call<UserIdCheckResponse>, response: Response<UserIdCheckResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.isAvailable == true) {
                        Toast.makeText(this@RegisterActivity, "사용 가능한 id입니다", Toast.LENGTH_SHORT).show()
                        isUserIdChecked = true
                    } else {
                        Toast.makeText(this@RegisterActivity, "사용 중인 id입니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "서버 에러 발생", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserIdCheckResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "통신 에러 발생: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
