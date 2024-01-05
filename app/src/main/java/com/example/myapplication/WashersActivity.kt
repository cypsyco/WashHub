package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WashersActivity : AppCompatActivity() {

    private val washerList = listOf(
        Washer(1, "Dryer 1", "USED"),
        Washer(2, "Dryer 2", "AVAILABLE"),
        // Add other dryers here...
    )



//    val call = RetrofitClient.instance.getWashers()
//    call.enqueue(object: Callback<List<Washer>> {
//        override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
//            if (response.isSuccessful) {
//                val fetchedList = response.body() // 받아온 데이터는 userList에 저장됩니다.
//                fetchedList?.forEach { washer ->
//                    Log.d("WasherList", washer.toString())
//                    // 여기서 "UserList"는 태그로, "User Name: ${user.name}"은 출력할 내용입니다.
//                    // 필요에 따라 로그 형식을 변경할 수 있습니다.
//                }
//                // userList를 사용하여 작업을 수행합니다.
//            } else {
//                // 실패 시 처리
//            }
//        }
//
//        override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
//            Log.e("WasherList", "Failed: ${t.message}")
//        }
//    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val recyclerView = findViewById<RecyclerView>(R.id.washers)

        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = WasherAdapter(washerList)
    }
}

data class Washer(
    val id: Int,
    val washerName: String,
    val washerState: String
)
