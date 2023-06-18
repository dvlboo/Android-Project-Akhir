package com.example.YANK_application.uitel

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.YANK_application.R
import com.example.YANK_application.activity.HomeActivity
import com.example.YANK_application.activity.LoginActivity
import com.example.YANK_application.activity.MenuActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000 // delay 3 detik
    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    lateinit var userId: String
    var meja: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        // auth untuk mendapatkan data akun
        auth = FirebaseAuth.getInstance()
        // store untuk mendapatkan data firestore
        store = FirebaseFirestore.getInstance()


        Handler().postDelayed({
            cekLogin()
            finish()
        }, SPLASH_TIME_OUT)
    }

    // ketika user sudah login bakal balik lagi ke home
    fun cekLogin() {
        if (auth.currentUser != null) {
            userId = auth.currentUser!!.uid
            val documentReference: DocumentReference = store.collection("users").document(userId)
            documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    meja = documentSnapshot.getString("Meja").toString()

                    if (meja == "") {
                        Intent(this, HomeActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Intent(this, MenuActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    }
                }
            })
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}