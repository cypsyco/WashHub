package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Threads
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import me.angrybyte.circularslider.CircularSlider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeSetActivity : ComponentActivity() {

    fun mapSliderValueToTime(angle: Double): Int {
        val maxAngle = 2 * Math.PI
        val maxTime = 90 * 60

        var adjustedAngle: Double
        if (angle>Math.PI/2){
            adjustedAngle = angle-(Math.PI/2)
        }else{
            adjustedAngle = angle+(Math.PI*3/2)
        }
        val time = ((adjustedAngle) / maxAngle * maxTime).toInt()
        Log.d("slidervalue","${angle}")

        return time
    }

    fun formatTime(time: Int): String {
        val minutes = time / 60
        val seconds = time % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    var settime = 60*60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_set)

        val receivedWasherName = intent.getStringExtra("washername")
        val receivedWasherId = intent.getIntExtra("washerid", -1)
        val userid = intent.getStringExtra("userid")
        val toolbardormText = intent.getStringExtra("toolbardormText")

        val toolbartitle = findViewById<TextView>(R.id.toolBarTitle)
        toolbartitle.text = receivedWasherName
        val toolbardorm = findViewById<TextView>(R.id.toolBarDorm)
        toolbardorm.text = ""
        val backbtn = findViewById<ImageButton>(R.id.toolBarBtn)
        backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val timesettext = findViewById<TextView>(R.id.timesetText)

        val circularSlider = findViewById<CircularSlider>(R.id.circular)


        val greyColor = ContextCompat.getColor(this, R.color.grey)
        circularSlider.setBorderColor(greyColor)


        circularSlider.setPosition(1.0/3.0)

        circularSlider.setOnSliderMovedListener(object : CircularSlider.OnSliderMovedListener {
            override fun onSliderMoved(pos: Double) {
                Log.d("pos", pos.toString())

                val angle = -(pos-0.25) * 2 * Math.PI
                settime = mapSliderValueToTime(angle)
                timesettext.text = formatTime(settime)
            }
        })

        val timesetbtn = findViewById<Button>(R.id.timesetbtn)
        timesetbtn.setOnClickListener {

            val cancelCall = userid?.let { RetrofitClient.instance.cancelWasherReservation(UserId(it)) }
            cancelCall?.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful && response.body()?.message == true) {
                    } else {
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.e("TimeSetActivity", "Cancel reservation call failed: ${t.message}")
                }
            })


            val starttime: Long = System.currentTimeMillis()
            val setTimeLong = settime.toLong() * 1000
            val timeSet = userid?.let { it1 -> TimeSet(starttime, setTimeLong, it1) }

            val call = timeSet?.let { it1 ->
                RetrofitClient.instance.updateWasherStatus(receivedWasherId,
                    it1
                )
            }

            if (call != null) {
                call.enqueue(object : Callback<WasherStatusResponse> {
                    override fun onResponse(
                        call: Call<WasherStatusResponse>,
                        response: Response<WasherStatusResponse>
                    ) {}

                    override fun onFailure(call: Call<WasherStatusResponse>, t: Throwable) {
                        Log.e("TimeSetActivity", "Retrofit call failed: ${t.message}")
                    }
                })
            }

            val intent = Intent(this@TimeSetActivity, WashersActivity::class.java)
            intent.putExtra("userid", userid)
            intent.putExtra("toolbardormText",toolbardormText)
            startActivity(intent)
            finish()
        }
    }
}