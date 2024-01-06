package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WashersActivity : AppCompatActivity() {

    private var washerList = mutableListOf<Washer>()
//    = listOf(
//        Washer(1, "Dryer 1", "USED"),
//        Washer(2, "Dryer 2", "AVAILABLE"),
//        // Add other dryers here...
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_washers)


        val recyclerView = findViewById<RecyclerView>(R.id.washers)

        val layoutManager = LinearLayoutManager(this)

        val call = RetrofitClient.instance.getWashers()
        call.enqueue(object: Callback<List<Washer>> {
        override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
            if (response.isSuccessful) {
                Log.d("WasherList", response.body().toString())
                val fetchedList = response.body()
                fetchedList?.let{
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
}

