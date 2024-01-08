package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WashersActivity : AppCompatActivity() {

    private var washerList = mutableListOf<Washer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washers)

        val toolbartitle = findViewById<TextView>(R.id.toolBarTitle)
        toolbartitle.text = "세탁기"
        val backbtn = findViewById<ImageButton>(R.id.toolBarBtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.washers)

        val layoutManager = LinearLayoutManager(this)

        val call = RetrofitClient.instance.getWashers()
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
        val intent = Intent(this, SelectActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}

