package com.example.myapplication.ui.select

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.NavigateActivity
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

        val navigateActivity = activity as? NavigateActivity
//        val toolbardorm = navigateActivity?.getToolbarDorm()
//        if (toolbardorm != null) {
//            toolbardorm.setTextColor(Color.BLACK)
//            toolbardorm.isClickable = true
//        }

        val buttonWasher = root.findViewById<Button>(R.id.button_washer)
        val buttonDryer = root.findViewById<Button>(R.id.button_dryer)

        buttonWasher.setOnClickListener {
//            val navigateActivity = activity as? NavigateActivity
            val toolbardormText = navigateActivity?.getToolbarDormText()
            val intent = Intent(requireContext(), WashersActivity::class.java)
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
