package com.example.myapplication.ui.using

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Dryer
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.Washer
import com.example.myapplication.WasherAdapter
import com.example.myapplication.databinding.FragmentUsingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsingFragment : Fragment() {

    private var _binding: FragmentUsingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var usingList = mutableListOf<Washer>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = root.findViewById<RecyclerView>(R.id.usings)
        val layoutManager = LinearLayoutManager(requireContext())

//        TODO("song: 상단바 기숙사 바꾸기 비활성화")
//        val navigateActivity = activity as? NavigateActivity
//        val toolbardorm = navigateActivity?.getToolbarDorm()
//        if (toolbardorm != null) {
//            toolbardorm.setTextColor(Color.TRANSPARENT)
//            toolbardorm.
//        }

//        TODO("jeon: userid에 해당하는 washers 갖고 오는 걸로 변경")
        val call: Call<List<Washer>> = RetrofitClient.instance.getWashers()

        call.enqueue(object : Callback<List<Washer>> {
            override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
                if (response.isSuccessful) {
                    Log.d("UsingList", response.body().toString())
                    val fetchedList = response.body()
                    fetchedList?.let {
                        usingList.clear()
                        usingList.addAll(it)

                    }
                    Log.d("UsingList", usingList.toString())

//        TODO("jeon: userid에 해당하는 dryers갖고 오는 걸로 변경")
                    val call2: Call<List<Dryer>> = RetrofitClient.instance.getDryers()

                    call2.enqueue(object : Callback<List<Dryer>> {
                        override fun onResponse(call: Call<List<Dryer>>, response: Response<List<Dryer>>) {
                            if (response.isSuccessful) {
                                val fetchedList = response.body()
                                fetchedList?.forEach { dryer->
                                    usingList.add(Washer(dryer.id+1000,dryer.dryername,dryer.dryerstatus,dryer.starttime,dryer.settime))
                                    Log.d("UsingList2", dryer.toString())
                                    Log.d("UsingList2", usingList.toString())
                                }
                            } else {
                                // 실패 시 처리
                                Log.d("UsingList2", usingList.toString())
                            }
                        }
                        override fun onFailure(call: Call<List<Dryer>>, t: Throwable) {
                            Log.e("UsingList2", "Failed: ${t.message}")
                        }
                    })
                    recyclerView.adapter?.notifyDataSetChanged()
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = UsingAdapter(usingList)
                } else {
                    // 실패 시 처리
                    Log.d("UsingList", usingList.toString())
                }
            }
            override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
                Log.e("UsingList", "Failed: ${t.message}")
            }
        })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}