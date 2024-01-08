package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.databinding.ActivityNavigateBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NavigateActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigateBinding

    private var userid: String? = null
    private var password: String? = null
    private var username: String? = null
    private var dormitory: String? = null
    private var gender: String? = null
    private var image: String? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.appBarNavigate.toolbar

        setSupportActionBar(binding.appBarNavigate.toolbar)

        val title = toolbar.findViewById<TextView>(R.id.toolbarTitle)
        val toolbarbtn = toolbar.findViewById<ImageButton>(R.id.toolbarBtn)
        toolbarbtn.setImageDrawable(null)

        val toolbardorm = toolbar.findViewById<TextView>(R.id.toolbarDorm)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)

        val headerView = navView.getHeaderView(0) // 헤더 레이아웃 가져오기

        val sidename = headerView.findViewById<TextView>(R.id.sidename_tv)
        val sidedorm = headerView.findViewById<TextView>(R.id.sidedorm_tv)
        val sideimg = headerView.findViewById<CircleImageView>(R.id.headerimageView)
        val sideeditbtn = headerView.findViewById<ImageButton>(R.id.editBtn)

        userid = intent.getStringExtra("userid")//?:"asdf"

        //사용자 정보 불러오기
        userid?.let {
            RetrofitClient.instance.getUserDetails(it)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            response.body()?.let { user ->
                                this@NavigateActivity.userid = user.userid
                                this@NavigateActivity.password = user.pw
                                this@NavigateActivity.username = user.username
                                this@NavigateActivity.dormitory = user.dormitory
                                this@NavigateActivity.gender = user.gender
                                this@NavigateActivity.image = user.image
                                sidename.text = username + "님"
                                sidedorm.text = dormitory
                                toolbardorm.text = dormitory
                                val decodedBytes = Base64.decode(image, Base64.DEFAULT)
                                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0 , decodedBytes.size)

                                sideimg.setImageBitmap(decodedBitmap)
//                                Glide.with(this)
//                                    .load(decodedBitmap)
//                                    .into(sideimg)
                                Log.d("responsesuccessful in SelectScreen", user.toString())
                                Toast.makeText(this@NavigateActivity, username, Toast.LENGTH_SHORT).show()

                            }
                        } else {
                            Toast.makeText(this@NavigateActivity, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@NavigateActivity, "에러: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.e("SelectActivity", "에러: ${t.message}")
                    }

                })
        } ?: run {
            Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
        }

        val navController = findNavController(R.id.nav_host_fragment_content_navigate)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_select, R.id.nav_using, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.apply{
            setDisplayShowTitleEnabled(false)
        }

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            val navtitle = when (destination.id) {
                R.id.nav_select -> getString(R.string.menu_select)
                R.id.nav_using -> "사용중 및 예약 현황"
                R.id.nav_slideshow -> getString(R.string.menu_slideshow)
                else -> getString(R.string.app_name)
            }
            title.text = navtitle
        }

        sideeditbtn.setOnClickListener{
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("userid", userid)
            startActivity(intent)
        }

        toolbardorm.setOnClickListener{
            val popup = PopupMenu(this, toolbardorm)
            popup.menuInflater.inflate(R.menu.navigate, popup.menu)
            if (gender=="남자") {
                popup.menu?.findItem(R.id.action_settings)?.title = "사랑관"
                popup.menu?.findItem(R.id.action_settings2)?.title = "소망관"
            } else {
                popup.menu?.findItem(R.id.action_settings)?.title = "아름관"
                popup.menu?.findItem(R.id.action_settings2)?.title = "나래관"
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_settings -> {
                        // 메뉴 아이템 클릭 시 동작할 내용 작성
                        val newdorm = if (gender == "남자") "사랑관" else "아름관"
                        toolbardorm.text = newdorm
                        sidedorm.text = newdorm
//                        TODO("{userid}의 기숙사 선택하는 걸로{toolbardorm.text} db dorm 업데이트")
                        true
                    }
                    R.id.action_settings2 -> {
                        // 다른 메뉴 아이템에 대한 동작
                        val newdorm = if (gender == "남자") "소망관" else "나래관"
                        toolbardorm.text = newdorm
                        sidedorm.text = newdorm
//                        TODO("{userid}의 기숙사 선택하는 걸로{toolbardorm.text} db dorm 업데이트")
                        true
                    }
                    // 추가적인 메뉴 아이템 핸들링 가능
                    else -> false
                }
            }
            popup.show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // 이전에 있던 옵션 메뉴 제거
        menu.clear()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigate)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getToolbarDormText(): String {
        return binding.appBarNavigate.toolbar.findViewById<TextView>(R.id.toolbarDorm).text.toString()
    }

    fun getToolbarDorm(): TextView {
        return binding.appBarNavigate.toolbar.findViewById<TextView>(R.id.toolbarDorm)
    }

    fun getUserId(): String? {
        return userid
    }
}