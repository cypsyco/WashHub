package com.example.myapplication.ui.reserved

import android.os.Bundle
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
import com.example.myapplication.Dryer
import com.example.myapplication.NavigateActivity
import com.example.myapplication.R
import com.example.myapplication.ReservationAdapter
import com.example.myapplication.User
import com.example.myapplication.Washer
import com.example.myapplication.databinding.FragmentReservedBinding
import org.w3c.dom.Text

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
            // TODO{"song: 첫번째면 사용하기")
            Toast.makeText(requireContext(),"앞선 대기자가 있습니다.",Toast.LENGTH_SHORT).show()
        }
        val washerCancelBtn = root.findViewById<TextView>(R.id.washerClearbtn)
        washerCancelBtn.setOnClickListener{
            // TODO("song: 예약취소 누르면 리스트 초기화")
            // TODO("jeon: reservation table에서 지우기")
        }

//        TODO("userid넘겨주면 예약중인 세탁기 정보 다 받아오기")

        val adapter = userid?.let { ReservationAdapter(rsvForWasherList, it) }
        washerrecyclerView.adapter = adapter

        ///////////////////////////////////////////////

        val dryerrecyclerView = root.findViewById<RecyclerView>(R.id.reserved_people_for_dryer)
        dryerrecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dryerBtn = root.findViewById<LinearLayout>(R.id.rsvDryerCard)
        dryerBtn.setOnClickListener{
            // TODO{"song: 첫번째면 사용하기")
            Toast.makeText(requireContext(),"앞선 대기자가 있습니다.",Toast.LENGTH_SHORT).show()
        }
        val dryerCancelBtn = root.findViewById<TextView>(R.id.dryerClearbtn)
        washerCancelBtn.setOnClickListener{
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