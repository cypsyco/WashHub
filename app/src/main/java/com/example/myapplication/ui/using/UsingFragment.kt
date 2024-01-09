package com.example.myapplication.ui.using

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Dryer
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.Washer
import com.example.myapplication.databinding.FragmentUsingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsingFragment : Fragment() {

    private var _binding: FragmentUsingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var usingWasherList = mutableListOf<Washer>()
    private var usingDryerList = mutableListOf<Dryer>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = root.findViewById<RecyclerView>(R.id.using_washers)
        val layoutManager = LinearLayoutManager(requireContext())

        val navigateActivity = activity as? NavigateActivity
        val userid = navigateActivity?.getUserId()

//        TODO("song: 상단바 기숙사 바꾸기 비활성화")
//        val navigateActivity = activity as? NavigateActivity
//        val toolbardorm = navigateActivity?.getToolbarDorm()
//        if (toolbardorm != null) {
//            toolbardorm.setTextColor(Color.TRANSPARENT)
//            toolbardorm.
//        }

        // 'UsingFragment' 클래스 내부
        if (userid != null) {
            val call: Call<List<Washer>> = RetrofitClient.instance.getWashersByUser(userid)

            call.enqueue(object : Callback<List<Washer>> {
                override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
                    if (response.isSuccessful) {
                        val fetchedList = response.body()
                        fetchedList?.let {
                            usingWasherList.clear()
                            usingWasherList.addAll(it)
                        }
                        Log.d("UsingList", usingWasherList.toString())
                        recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        // 실패 시 처리
                        Log.d("UsingList", "Response not successful")
                    }
                }
                override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
                    Log.e("UsingList", "Failed: ${t.message}")
                }
            })
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = UsingWasherAdapter(usingWasherList)

        val recyclerView2 = root.findViewById<RecyclerView>(R.id.using_dryers)
        val layoutManager2 = LinearLayoutManager(requireContext())

        // 'UsingFragment' 클래스 내부
        if (userid != null) {
            val call2: Call<List<Dryer>> = RetrofitClient.instance.getDryersByUser(userid)

            call2.enqueue(object : Callback<List<Dryer>> {
                override fun onResponse(call: Call<List<Dryer>>, response: Response<List<Dryer>>) {
                    if (response.isSuccessful) {
                        val fetchedList = response.body()
                        fetchedList?.let {
                            usingDryerList.clear()
                            usingDryerList.addAll(it)
                        }
                        Log.d("UsingList", usingDryerList.toString())
                        recyclerView.adapter?.notifyDataSetChanged()
                    } else {
                        // 실패 시 처리
                        Log.d("UsingList", "Response not successful")
                    }
                }
                override fun onFailure(call: Call<List<Dryer>>, t: Throwable) {
//                    TODO("Not yet implemented")
                }
            })
        }

        recyclerView2.layoutManager = layoutManager2
        recyclerView2.adapter = UsingDryerAdapter(usingDryerList)

//        val call: Call<List<Washer>> = RetrofitClient.instance.getWashers()
//
//        call.enqueue(object : Callback<List<Washer>> {
//            override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
//                if (response.isSuccessful) {
//                    Log.d("UsingList", response.body().toString())
//                    val fetchedList = response.body()
//                    fetchedList?.let {
//                        usingList.clear()
//                        usingList.addAll(it)
//
//                    }
//                    Log.d("UsingList", usingList.toString())
//
////        TODO("jeon: userid에 해당하는 dryers갖고 오는 걸로 변경")
//                    val call2: Call<List<Dryer>> = RetrofitClient.instance.getDryers()
//
//                    call2.enqueue(object : Callback<List<Dryer>> {
//                        override fun onResponse(call: Call<List<Dryer>>, response: Response<List<Dryer>>) {
//                            if (response.isSuccessful) {
//                                val fetchedList = response.body()
//                                fetchedList?.forEach { dryer->
//                                    usingList.add(Washer(dryer.id+1000,dryer.dryername,dryer.dryerstatus,dryer.starttime,dryer.settime))
//                                    Log.d("UsingList2", dryer.toString())
//                                    Log.d("UsingList2", usingList.toString())
//                                }
//                            } else {
//                                // 실패 시 처리
//                                Log.d("UsingList2", usingList.toString())
//                            }
//                        }
//                        override fun onFailure(call: Call<List<Dryer>>, t: Throwable) {
//                            Log.e("UsingList2", "Failed: ${t.message}")
//                        }
//                    })
//                    recyclerView.adapter?.notifyDataSetChanged()
//                    recyclerView.layoutManager = layoutManager
//                    recyclerView.adapter = UsingAdapter(usingList)
//                } else {
//                    // 실패 시 처리
//                    Log.d("UsingList", usingList.toString())
//                }
//            }
//            override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
//                Log.e("UsingList", "Failed: ${t.message}")
//            }
//        })

//        val mywasher = root.findViewById<TextView>(R.id.myWasher)
//        val mydryer = root.findViewById<TextView>(R.id.myDryer)
//
//        if (userid != null) {
//            RetrofitClient.instance.getUserReservations(userid).enqueue(object : Callback<List<ReservationResponse>> {
//                override fun onResponse(call: Call<List<ReservationResponse>>, response: Response<List<ReservationResponse>>) {
//                    if (response.isSuccessful) {
//                        val fetchedList = response.body()
//                        fetchedList?.forEach { using->
//                            mywasher.text = using.washername?: "-"
//                            mydryer.text = using.dryername?: "-"
//                        }
//                    } else {
//                        // 실패 시 처리
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ReservationResponse>>, t: Throwable) {
////                    TODO("Not yet implemented")
//                }
//            })
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}