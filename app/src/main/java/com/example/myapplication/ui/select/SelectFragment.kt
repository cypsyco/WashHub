package com.example.myapplication.ui.select

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.RetrofitClient
import com.example.myapplication.User
import com.example.myapplication.WashersActivity
import com.example.myapplication.databinding.FragmentSelectBinding
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

        val buttonWasher = root.findViewById<Button>(R.id.button_washer)
        val buttonDryer = root.findViewById<Button>(R.id.button_dryer)

        buttonWasher.setOnClickListener {
            Toast.makeText(requireContext(), "Washer clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), WashersActivity::class.java)
            val receivedIntent = activity?.intent
            val userid = receivedIntent?.getStringExtra("userid")
            intent.putExtra("userid", userid)
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
