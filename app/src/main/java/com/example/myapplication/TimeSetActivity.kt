package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import me.angrybyte.circularslider.CircularSlider

class TimeSetActivity : ComponentActivity() {

    fun mapSliderValueToTime(angle: Double): Int {
        val maxAngle = 2 * Math.PI // 360도에 해당하는 라디안 값
        val maxTime = 90 * 60 // 최대 시간 (90분)


        // 슬라이더 각도를 시간으로 변환
        val time = ((angle-2/Math.PI) / maxAngle * maxTime).toInt()
        Log.d("slidervalue","${angle}   ")

        return time
    }

    fun formatTime(time: Int): String {
        val minutes = time / 60
        val seconds = time % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_set)

        val toolbartitle = findViewById<TextView>(R.id.toolBarTitle)
        toolbartitle.text = "세탁기"
        val backbtn = findViewById<ImageButton>(R.id.toolBarBtn)
        backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val timesettext = findViewById<TextView>(R.id.timesetText)

        val circularSlider = findViewById<CircularSlider>(R.id.circular)

        circularSlider.setOnSliderMovedListener(object : CircularSlider.OnSliderMovedListener {
            override fun onSliderMoved(pos: Double) {
                Log.d("pos", pos.toString())
                val angle = -(pos-0.25) * 2 * Math.PI // 슬라이더의 각도를 라디안 값으로 변환
                time = mapSliderValueToTime(angle)
                timesettext.text = formatTime(time)
            }
        })
    }
}