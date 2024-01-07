package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

//        val startTime: Long = System.currentTimeMillis()
//        val setTime = 3600000L

        holder.btnAction.text = currentWasher.washerstatus

        if (currentWasher.washerstatus == "사용중"){
            val remainingTime = currentWasher.starttime + currentWasher.settime - System.currentTimeMillis()

            holder.mTimer = object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / (1000 * 60 * 60)
                    val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                    val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    holder.remainingTime.text = timeLeftFormatted
                }

                override fun onFinish() {
                    holder.remainingTime.text = ""
                }
            }
            holder.mTimer.start()
        }else{
            holder.remainingTime.text = ""
        }



        if (currentWasher.washerstatus.trim() != "사용 가능"){
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.btnAction.context,R.color.red))
            Log.d("Washerstatus",currentWasher.washerstatus+""+R.color.red.toString())
        }else{
            holder.btnAction.setBackgroundColor(ContextCompat.getColor(holder.btnAction.context,R.color.blue))
            Log.d("Washerstatus",currentWasher.washerstatus+""+R.color.blue.toString())
        }

        holder.btnAction.setOnClickListener {
            val intent = Intent(holder.itemView.context, TimeSetActivity::class.java)
            intent.putExtra("washername",currentWasher.washername)
            intent.putExtra("washerid", currentWasher.id)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return washerList.size
    }
}