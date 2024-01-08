package com.example.myapplication.ui.using

import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiResponse
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.Washer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsingAdapter(private val usingList: List<Washer>) : RecyclerView.Adapter<UsingAdapter.UsingViewHolder>() {

    class UsingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usingname: TextView = itemView.findViewById(R.id.txtUsingName)
        val remainingTime: TextView = itemView.findViewById(R.id.txtuserRemainingTime)
        val dorm: TextView = itemView.findViewById(R.id.txtDorm)
        val usingcard: LinearLayout = itemView.findViewById(R.id.usingItem)
        lateinit var mTimer: CountDownTimer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.using_item, parent, false)
        return UsingViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UsingViewHolder, position: Int) {
        val currentUsing = usingList[position]
        holder.usingname.text = currentUsing.washername
        holder.dorm.text = "(사랑관)"  //TODO("기숙사이름 알맞게")
        val usingid = currentUsing.id


        val remainingTime = currentUsing.starttime + currentUsing.settime - System.currentTimeMillis()

        if(remainingTime>0){
            holder.mTimer = object : CountDownTimer(remainingTime, 1000) {
                var isOneSecondLeft = false

                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished <= 1000 && !isOneSecondLeft) {
                        isOneSecondLeft = true
                        Toast.makeText(holder.itemView.context, "세탁이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        val call = if (usingid<1000)RetrofitClient.instance.endWasherSession(usingid) else{RetrofitClient.instance.endWasherSession(usingid)}   // TODO("else일 때 endDryerSession 으로 바꾸기")
                        call.enqueue(object :
                            Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful && response.body()?.message == true) {
//                                Toast.makeText(holder.itemView.context, "세탁기 상태가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
//                                currentUsing.washerstatus = "사용 가능"

                                    notifyItemChanged(position)
                                } else {
//                                Toast.makeText(holder.itemView.context, "세탁기 상태 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                    Log.e("WasherUpdateError", "Response Code: ${response.code()} Error Body: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                            Toast.makeText(holder.itemView.context, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                Log.e("NetworkError", "Failed to connect to the server", t)
                            }
                        })
                    }

                    val hours = millisUntilFinished / (1000 * 60 * 60)
                    val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                    val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    Log.d("timeLeftFormatted", holder.usingname.toString()+" "+timeLeftFormatted)
                    holder.remainingTime.text = timeLeftFormatted
                }

                override fun onFinish() {
                    holder.remainingTime.text = ""
                }
            }
        }else{
            holder.remainingTime.text=""
        }


        if (usingid<1000){
            holder.usingcard.setBackgroundColor(ContextCompat.getColor(holder.usingcard.context, R.color.lightblue))
            Log.d("color",usingid.toString()+" "+holder.usingcard.background)
        }else{
            holder.usingcard.setBackgroundColor(ContextCompat.getColor(holder.usingcard.context, R.color.lightred))
            Log.d("color",usingid.toString()+" "+holder.usingcard.background)
        }
    }

    override fun getItemCount(): Int {
        return usingList.size
    }
}