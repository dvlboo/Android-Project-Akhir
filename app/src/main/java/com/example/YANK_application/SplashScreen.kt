package com.example.YANK_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000 // delay 3 detik
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            cekLogin()
            finish()
        }, SPLASH_TIME_OUT)
    }

    // ketika user sudah login bakal balik lagi ke home
     fun cekLogin() {
        if(auth.currentUser != null){
            Intent(this, NewHomeActivity::class.java).also{
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        } else {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}