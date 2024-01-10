package com.example.myapplication.ui.reserved

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiResponse
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.ReservationAdapter
import com.example.myapplication.RetrofitClient
import com.example.myapplication.User
import com.example.myapplication.UserId
import com.example.myapplication.Washer
import com.example.myapplication.databinding.FragmentReservedBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservedFragment : Fragment() {

    private var _binding: FragmentReservedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var rsvForWasherList = mutableListOf<String>()
    private var rsvForDryerList = mutableListOf<String>()

    private var isCanceled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        rsvForWasherList.add("가영")
//        rsvForWasherList.add("firstname")
//        rsvForWasherList.add("나영")
//
//        rsvForDryerList.add("firstname")
//        rsvForDryerList.add("다영")
//        rsvForDryerList.add("라영")

        _binding = FragmentReservedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navigateActivity = activity as? NavigateActivity
        val userid = navigateActivity?.getUserId()
        var username = ""

        userid?.let {
            RetrofitClient.instance.getUserDetails(it)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            response.body()?.let { user ->
                                username = user.username
                            }
                        } else {
                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }
                })
        } ?: run {
        }

        //////////////////////////////////////////////////////

        val washerrecyclerView = root.findViewById<RecyclerView>(R.id.reserved_people_for_washer)
        washerrecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val washerBtn = root.findViewById<LinearLayout>(R.id.rsvWasherCard)
        val washername = root.findViewById<TextView>(R.id.rsvWasherName)
        val washerdorm = root.findViewById<TextView>(R.id.rsvWasherdorm)
        val washerremaintime = root.findViewById<TextView>(R.id.rsvWasherTime)
        var washerid:Int? = null

        val scale_up = AnimationUtils.loadAnimation(context, R.anim.scale_up)
//        washerrecyclerView.visibility=View.INVISIBLE
        washerBtn.animation = scale_up

//        trans_anim.start()

        val washerCancelBtn = root.findViewById<TextView>(R.id.washerClearbtn)
        washerCancelBtn.setOnClickListener{
            userid?.let { it1 -> UserId(it1) }?.let { it2 ->
                RetrofitClient.instance.cancelWasherReservation(it2)
                    .enqueue(object : Callback<ApiResponse>{
                        override fun onResponse(
                            call: Call<ApiResponse>,
                            response: Response<ApiResponse>
                        ) {
                            rsvForWasherList.clear()
                            washername.text = " - "
                            washerdorm.text = ""
                            washerremaintime.text = ""
                        }

                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                            TODO("Not yet implemented")
                        }

                    })
            }
            isCanceled = true
            rsvForWasherList.clear()
        }

        //write code here
        userid?.let { userId ->
            // 사용자가 예약한 세탁기의 모든 정보 조회
            RetrofitClient.instance.getWasherReservationsByUser(userId)
                .enqueue(object : Callback<List<Washer>> {
                    override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
                        if (response.isSuccessful) {
                            // 성공적으로 데이터를 받아온 경우
                            response.body()?.forEach { washer ->
                                Log.d("ReservedFragment", "Reserved Washers: $washer")
                                washerid = washer.id
                                washername.text = washer.washername
                                washerdorm.text = washer.dorm
                                washerremaintime.text = ""


                                if (washer.washerstatus == "사용중") {
                                    val remainingTime = washer.starttime + washer.settime - System.currentTimeMillis()

                                    val timer = object : CountDownTimer(remainingTime, 1000) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            if (isCanceled) {
                                                cancel()
                                                onFinish()
                                                return
                                            }
                                            val hours = millisUntilFinished / (1000 * 60 * 60)
                                            val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                                            val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                                            val timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                                            washerremaintime.text = timeLeftFormatted
                                        }

                                        override fun onFinish() {
                                            washerremaintime.text = ""

                                            if (rsvForWasherList.size>0 && rsvForWasherList[0] == username){
                                                Log.d("anim", "rsvForWasherList[0]: ${rsvForWasherList[0]} username: ${username}")
                                                val scale_anim = AnimationUtils.loadAnimation(context, R.anim.scale_up)
                                                washerBtn.animation = scale_anim
                                                scale_anim.start()

                                                val washertexts = root.findViewById<LinearLayout>(R.id.rsvWasherTexts)

                                                washername.visibility = View.INVISIBLE
                                                washerdorm.visibility = View.INVISIBLE
                                                washerremaintime.visibility = View.INVISIBLE
                                                washertexts.visibility = View.VISIBLE

//                                                scale_anim.start()

                                            }
                                            // Add any additional code to handle the finish event
                                        }
                                    }
                                    timer.start()
                                } else {
                                    washerremaintime.text = ""
                                }




                                Log.d("washerid",washerid.toString())
                                washerid?.let {
                                    RetrofitClient.instance.getUsernamesByWasher(it)
                                        .enqueue(object : Callback<List<String>> {
                                            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                                                if (response.isSuccessful) {
                                                    // 성공적으로 데이터를 받아온 경우
                                                    rsvForWasherList.clear()
                                                    response.body()?.let { usernames ->
                                                        rsvForWasherList = usernames.toMutableList()
                                                        val adapter = userid?.let { ReservationAdapter(rsvForWasherList, it) }
                                                        washerrecyclerView.adapter = adapter
                                                        Log.d("ReservedFragment", "Usernames for Washer $washerid: $usernames")
                                                        Log.d("rsvForWasherList", rsvForWasherList.toString())
                                                    }
                                                } else {
                                                    // 서버로부터 에러 응답을 받은 경우
                                                    Log.e("ReservedFragment", "Error: ${response.errorBody()?.string()}")
                                                }
                                            }

                                            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                                                // 네트워크 오류 또는 요청 실패
                                                Log.e("ReservedFragment", "Failed to fetch usernames for washer", t)
                                            }
                                        })
                                }
                                val adapter = userid?.let { ReservationAdapter(rsvForDryerList, it) }
                                washerrecyclerView.adapter = adapter
                            }
                        } else {
                            // 서버로부터 에러 응답을 받은 경우
                            Log.e("ReservedFragment", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
                        // 네트워크 오류 또는 요청 실패
                        Log.e("ReservedFragment", "Failed to fetch reserved washers", t)
                    }
                })

//            val washerId = 1 // 예시로 1을 사용


        }


        /////////////////////////////////////////

        val dryerrecyclerView = root.findViewById<RecyclerView>(R.id.reserved_people_for_dryer)
        dryerrecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dryerBtn = root.findViewById<LinearLayout>(R.id.rsvDryerCard)
        dryerBtn.setOnClickListener{
            // TODO{"song: 첫번째면 사용하기")
            Toast.makeText(requireContext(),"앞선 대기자가 있습니다.",Toast.LENGTH_SHORT).show()
        }
        val dryerCancelBtn = root.findViewById<TextView>(R.id.dryerClearbtn)
        dryerCancelBtn.setOnClickListener{
            // TODO("song: 예약취소 누르면 리스트 초기화")
            // TODO("jeon: reservation table에서 지우기")
        }

        val dadapter = userid?.let { ReservationAdapter(rsvForDryerList, it) }
        dryerrecyclerView.adapter = dadapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}