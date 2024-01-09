package com.example.myapplication.ui.select

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.User
import com.example.myapplication.Washer
import com.example.myapplication.WashersActivity
import com.example.myapplication.databinding.FragmentSelectBinding
import com.example.myapplication.publicDorm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectFragment : Fragment() {

    private var _binding: FragmentSelectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var allNum = 0
        var availableNum = 0

        val navigateActivity = activity as? NavigateActivity
//        val toolbardorm = navigateActivity?.getToolbarDorm()
//        if (toolbardorm != null) {
//            toolbardorm.setTextColor(Color.BLACK)
//            toolbardorm.isClickable = true
//        }

        val call: Call<List<Washer>> = when (publicDorm) {
            "사랑관" -> RetrofitClient.instance.getWashersDorm1()
            "소망관" -> RetrofitClient.instance.getWashersDorm2()
            "아름관" -> RetrofitClient.instance.getWashersDorm3()
            "나래관" -> RetrofitClient.instance.getWashersDorm4()
            else -> RetrofitClient.instance.getWashers() // 기본값
        }


        Log.d("publicDorm", publicDorm)
        call.enqueue(object : Callback<List<Washer>> {
            override fun onResponse(call: Call<List<Washer>>, response: Response<List<Washer>>) {
                if (response.isSuccessful) {
                    Log.d("WasherList", response.body().toString())
                    val fetchedList = response.body()
                    fetchedList?.forEach {
                        allNum+=1
                        if (it.washerstatus == "사용 가능"){
                            availableNum+=1
                        }

                        Log.d("num","( ${availableNum} / ${allNum} )" )
                    }
                    Log.d("num","( ${availableNum} / ${allNum} )" )
                    val howmany = root.findViewById<TextView>(R.id.howManyAvailableWasher)
                    howmany.text = "( ${availableNum} / ${allNum} )"
                } else {
                    // 실패 시 처리
                }
            }

            override fun onFailure(call: Call<List<Washer>>, t: Throwable) {
                Log.e("WasherList", "Failed: ${t.message}")
            }
        })

        val buttonWasher = root.findViewById<CardView>(R.id.select_washer)
        val buttonDryer = root.findViewById<CardView>(R.id.select_dryer)

        buttonWasher.setOnClickListener {
//            val navigateActivity = activity as? NavigateActivity
            val intent = Intent(requireContext(), WashersActivity::class.java)
            val toolbardormText = navigateActivity?.getToolbarDormText()
            val receivedIntent = activity?.intent
            val userid = receivedIntent?.getStringExtra("userid")
            intent.putExtra("userid", userid)
            intent.putExtra("toolbardormText", toolbardormText)
            startActivity(intent)
        }


        buttonDryer.setOnClickListener {
            Toast.makeText(requireContext(), "Dryer clicked", Toast.LENGTH_SHORT).show()
        }

        // 사용자 정보 불러오기

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}