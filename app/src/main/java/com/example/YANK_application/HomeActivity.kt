package com.example.YANK_application

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.YANK_application.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private var backPressed: Long = 0

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags


    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity() // Mengakhiri aktivitas dan semua aktivitas di dalamnya
            super.onBackPressed()
        } else {
            showToast("Tekan lagi untuk keluar")
            backPressed = System.currentTimeMillis()
        }
    }

    fun logout(view: View) {
        //menghapus SharedPreferences agar tidak masuk ke Home lagi, tapi tetap masuk ke Login
//        val logindata = getSharedPreferences("login", MODE_PRIVATE)
//        val nodata = getSharedPreferences("no", MODE_PRIVATE)
//        val editor = logindata.edit()
//        editor.clear()
//        editor.apply()
//        val editor2 = nodata.edit()
//        editor2.clear()
//        editor2.apply()
        auth.signOut()
        Intent(this, LoginActivity::class.java).also{
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            showToast("Logout Berhasil")
        }
    }

}