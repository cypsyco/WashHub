package com.example.myapplication.ui.reserved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiResponse
import com.example.myapplication.Dryer
import com.example.myapplication.FullReservationResponse
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.ReservationAdapter
import com.example.myapplication.ReservationResponse
import com.example.myapplication.RetrofitClient
import com.example.myapplication.User
import com.example.myapplication.UserId
import com.example.myapplication.Washer
import com.example.myapplication.databinding.FragmentReservedBinding
import org.w3c.dom.Text
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rsvForWasherList.add("가영")
        rsvForWasherList.add("firstname")
        rsvForWasherList.add("나영")

        rsvForDryerList.add("firstname")
        rsvForDryerList.add("다영")
        rsvForDryerList.add("라영")

        _binding = FragmentReservedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val navigateActivity = activity as? NavigateActivity
        val userid = navigateActivity?.getUserId()

        //////////////////////////////////////////////////////

        val washerrecyclerView = root.findViewById<RecyclerView>(R.id.reserved_people_for_washer)
        washerrecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val washerBtn = root.findViewById<LinearLayout>(R.id.rsvWasherCard)
        washerBtn.setOnClickListener{
            // TODO{"song: 첫번째고 남은 시간이 없으면 사용하기")
            Toast.makeText(requireContext(),"앞선 대기자가 있습니다.",Toast.LENGTH_SHORT).show()
        }
        val washername = root.findViewById<TextView>(R.id.rsvWasherName)
        val washerdorm = root.findViewById<TextView>(R.id.rsvWasherdorm)
        val washerremaintime = root.findViewById<TextView>(R.id.rsvWasherTime)
        var washerid:Int? = null

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
            rsvForWasherList.clear()
        }

//        TODO("userid넘겨주면 예약중인 세탁기 정보 다 받아오기")
//        if (userid != null) {
//            RetrofitClient.instance.getUserReservations(userid)     // user가 예약하고 있는 세탁기 정보 받아서
//                .enqueue(object : Callback<List<FullReservationResponse>> {
//                    override fun onResponse(
//                        call: Call<List<FullReservationResponse>>,
//                        response: Response<List<FullReservationResponse>>
//                    ) {
//                        if (response.isSuccessful) {
//                            response.body()?.get(0)?.let {
//                                Log.d("response for getWashersByUser", response.body().toString())
//                                it.washer?.let { it1 ->
//                                    washerid = it1.id.toInt()
//                                    washername.text = it1.washername
//                                    washerdorm.text = it1.dorm
//                                }
//                            }
//                        } else {
//                            Log.e(
//                                "ReserveDialog",
//                                "서버 응답 실패: ${response.code()} - ${response.errorBody()?.string()}"
//                            )
//                            Toast.makeText(context, "예약 목록을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<FullReservationResponse>>, t: Throwable) {
//                        Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//        washerid?.let {
//            RetrofitClient.instance.getWasherReservations(it)   // 여기에 전달 -> 해당 세탁기 이용하고 있는 모든 이용자 정보 리사이클러뷰에 띄움
//                .enqueue(object : Callback<List<String>> {
//                    override fun onResponse(
//                        call: Call<List<String>>,
//                        response: Response<List<String>>
//                    ) {
//                        if (response.isSuccessful) {
//                            rsvForWasherList.clear()
//                            rsvForWasherList.addAll(response.body() ?: emptyList())
//                            washerrecyclerView.adapter?.notifyDataSetChanged()
//                        } else {
//                            Log.e(
//                                "ReserveDialog",
//                                "서버 응답 실패: ${response.code()} - ${response.errorBody()?.string()}"
//                            )
//                            Toast.makeText(context, "예약 목록을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
//                        Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
//                    }
//                })
//        }
//
//        val adapter = userid?.let { ReservationAdapter(rsvForWasherList, it) }
//        washerrecyclerView.adapter = adapter

        ///////////////////////////////////////////////

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