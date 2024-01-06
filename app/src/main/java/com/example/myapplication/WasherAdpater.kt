package com.example.myapplication

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDateTime

class WasherAdapter(private val washerList: List<Washer>) : RecyclerView.Adapter<WasherAdapter.WasherViewHolder>() {

    class WasherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val washername: TextView = itemView.findViewById(R.id.txtWasherName)
        val remainingTime: TextView = itemView.findViewById(R.id.txtRemainingTime)
        val btnAction: Button = itemView.findViewById(R.id.btnAction)
        lateinit var mTimer: CountDownTimer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return WasherViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: WasherViewHolder, position: Int) {
        val currentWasher = washerList[position]
        holder.washername.text = currentWasher.washername

        val startTime: Long = System.currentTimeMillis()
        val setTime = 3600000L

        val remainingTime = startTime + setTime - System.currentTimeMillis()

        holder.btnAction.text = currentWasher.washerstatus

        holder.mTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (1000 * 60 * 60)
                val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                holder.remainingTime.text = timeLeftFormatted
            }

            override fun onFinish() {
                holder.remainingTime.text = "done"
            }
        }
        holder.mTimer.start()


        if (currentWasher.washerstatus.trim() != "AVAILABLE"){
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.btnAction.context,R.color.red))
            Log.d("Washerstatus",currentWasher.washerstatus+""+R.color.red.toString())
        }else{
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.btnAction.context,R.color.blue))
            Log.d("Washerstatus",currentWasher.washerstatus+""+R.color.blue.toString())
        }

        holder.btnAction.setOnClickListener {
            // 여기에 버튼 클릭 시 동작을 정의합니다.
            // 예를 들어, 해당 Dryer에 대한 작업을 실행하거나 특정 기능을 수행할 수 있습니다.
        }
    }

    override fun getItemCount(): Int {
        return washerList.size
    }
}