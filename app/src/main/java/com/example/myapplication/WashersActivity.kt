package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WashersActivity : AppCompatActivity() {

    private var washerList = mutableListOf<Washer>()

    private var userid: String? = null
    private var password: String? = null
    private var username: String? = null
    private var dormitory: String? = null
    private var gender: String? = "true"
    private var image: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washers)

        val toolbartitle = findViewById<TextView>(R.id.toolBarTitle)
        toolbartitle.text = "세탁기"

        userid = intent.getStringExtra("userid")
        val toolbardormText = intent.getStringExtra("toolbardormText")

        val backbtn = findViewById<ImageButton>(R.id.toolBarBtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, NavigateActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("userid",userid)
            startActivity(intent)
            finish()
        }

        val toolbardorm = findViewById<TextView>(R.id.toolBarDorm)

        //사용자 정보 불러오기
        userid?.let {
            RetrofitClient.instance.getUserDetails(it)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            response.body()?.let { user ->
                                userid = user.userid
                                password = user.pw
                                username = user.username
                                dormitory = user.dormitory
                                gender = user.gender
                                image = user.image
                                toolbardorm.text = dormitory
//                                Toast.makeText(this, userid, Toast.LENGTH_SHORT).show()
                            }
                        } else {
//                            Toast.makeText(this@SelectActivity, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
//                        Toast.makeText(this@SelectActivity, "에러: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("SelectActivity", "에러: ${t.message}")
                    }

                })
        } ?: run {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
        }

        toolbardorm.setOnClickListener{
            val popup = PopupMenu(this, toolbardorm)
            popup.menuInflater.inflate(R.menu.navigate, popup.menu)
            if (gender=="남자") {
                popup.menu?.findItem(R.id.action_settings)?.title = "사랑관"
                popup.menu?.findItem(R.id.action_settings2)?.title = "소망관"
            } else {
                popup.menu?.findItem(R.id.action_settings)?.title = "아름관"
                popup.menu?.findItem(R.id.action_settings2)?.title = "나래관"
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_settings -> {
                        // 메뉴 아이템 클릭 시 동작할 내용 작성
                        val newdorm = if (gender == "남자") "사랑관" else "아름관"
                        toolbardorm.text = newdorm
//                        TODO("{userid}의 기숙사 선택하는 걸로{toolbardorm.text} db dorm 업데이트")
                        true
                    }
                    R.id.action_settings2 -> {
                        // 다른 메뉴 아이템에 대한 동작
                        val newdorm = if (gender == "남자") "소망관" else "나래관"
                        toolbardorm.text = newdorm
//                        TODO("{userid}의 기숙사 선택하는 걸로{toolbardorm.text} db dorm 업데이트")
                        true
                    }
                    // 추가적인 메뉴 아이템 핸들링 가능
                    else -> false
                }
            }
            popup.show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.washers)

        val layoutManager = LinearLayoutManager(this)

        val call: Call<List<Washer>> = when (toolbardormText) {
            "사랑관" -> RetrofitClient.instance.getWashersDorm1()
            "소망관" -> RetrofitClient.instance.getWashersDorm2()
            "아름관" -> RetrofitClient.instance.getWashersDorm3()
            "나래관" -> RetrofitClient.instance.getWashersDorm4()
            else -> RetrofitClient.instance.getWashers() // 기본값ㅁㄴ
        }

        call.enqueue(object : Callback<List<Washer>> {
            override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
                if (response.isSuccessful) {
                    Log.d("WasherList", response.body().toString())
                    val fetchedList = response.body()
                    fetchedList?.let {
                        washerList.clear()
                        washerList.addAll(it)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                    Log.d("WasherList2", washerList.toString())
                } else {
                    // 실패 시 처리
                }
            }

            override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
                Log.e("WasherList", "Failed: ${t.message}")
            }
        })
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = WasherAdapter(washerList)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NavigateActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("userid",userid)
        startActivity(intent)
        finish()
    }
}

