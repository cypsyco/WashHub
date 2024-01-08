package com.example.myapplication

import android.app.Dialog
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
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        var washerId: Int = 0
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
        holder.washerId = currentWasher.id

//        val startTime: Long = System.currentTimeMillis()
//        val setTime = 3600000L
//        val userid =

        holder.btnAction.text = currentWasher.washerstatus

        if (currentWasher.washerstatus == "사용중") {
            val remainingTime = currentWasher.starttime + currentWasher.settime - System.currentTimeMillis()

            holder.mTimer = object : CountDownTimer(remainingTime, 1000) {
                var isOneSecondLeft = false

                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished <= 1000 && !isOneSecondLeft) {
                        isOneSecondLeft = true
                        Toast.makeText(holder.itemView.context, "세탁이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        RetrofitClient.instance.endWasherSession(holder.washerId).enqueue(object : Callback<ApiResponse> {
                            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                                if (response.isSuccessful && response.body()?.message == true) {
                                    Toast.makeText(holder.itemView.context, "세탁기 상태가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                                    currentWasher.washerstatus = "사용 가능"
//                                    TODO("jeon 사용중 테이블 만들어서 시간 끝나면 사용중에서 지우기")
                                    notifyItemChanged(position)
                                } else {
                                    Toast.makeText(holder.itemView.context, "세탁기 상태 업데이트에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                    Log.e("WasherUpdateError", "Response Code: ${response.code()} Error Body: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                Toast.makeText(holder.itemView.context, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                Log.e("NetworkError", "Failed to connect to the server", t)
                            }
                        })
                    }

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
        } else {
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
            when (currentWasher.washerstatus) {
                "사용 가능" -> {
                    // 사용 가능일 때의 로직
                    val intent = Intent(holder.itemView.context, TimeSetActivity::class.java)
                    intent.putExtra("washername",currentWasher.washername)
                    intent.putExtra("washerid", currentWasher.id)
                    ContextCompat.startActivity(holder.itemView.context, intent, null)
                }
                "사용중" -> {
                    reserveDialog(holder.itemView.context, currentWasher.washername)
                    // 사용중일 때의 로직
                    Toast.makeText(holder.itemView.context, "기계가 사용 중입니다.", Toast.LENGTH_SHORT).show()
                }
                "수리중" -> {
                    // 수리중일 때의 로직
                    Toast.makeText(holder.itemView.context, "기계가 수리 중입니다. 다른 기계를 이용해 주세요.", Toast.LENGTH_SHORT).show()
                }
                "예약중" -> {
//                    if (currentWasher.using == userid)
//                    TODO("세탁기 db에 사용중인 사람 아이디 넣고 조건문 처리하기")
                    reserveDialog(holder.itemView.context, currentWasher.washername)
                    // 예약중일 때의 로직
                    Toast.makeText(holder.itemView.context, "기계가 예약되어 있습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 그 외의 경우
                    Toast.makeText(holder.itemView.context, "상태를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return washerList.size
    }

    fun reserveDialog(context:Context, washername: String){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_reservation)

        val dialogTitle = dialog.findViewById<TextView>(R.id.reserv_title)
        dialogTitle.text = washername + " 예약목록"
//        TODO("나중엔 세탁기 이름에서 기숙사 빼기")

        val recyclerView: RecyclerView = dialog.findViewById(R.id.reservations)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val reservedlist = mutableListOf<User>()
//        TODO("reservedlist db에서 갖고오기")
        reservedlist.add(User("estherjsong","123","testusername","아름관", "여자", ""))
        reservedlist.add(User("asdf","1234","testusername3","아름관", "여자", ""))
        reservedlist.add(User("qwewr","1234","testusername2","아름관", "여자", ""))

        val adapter = ReservationAdapter(reservedlist)
        recyclerView.adapter = adapter

        val addbtn = dialog.findViewById<ImageButton>(R.id.addBtn)
        addbtn.setOnClickListener{
            Toast.makeText(context,"예약되었습니다.",Toast.LENGTH_SHORT).show()
//            TODO("db reservedlist에 userid 추가")
        }

        dialog.show()
    }
}