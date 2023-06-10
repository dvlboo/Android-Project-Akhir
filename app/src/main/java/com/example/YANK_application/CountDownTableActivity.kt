package com.example.YANK_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.YANK_application.databinding.ActivityCountDownTableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import java.util.concurrent.TimeUnit

class CountDownTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountDownTableBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    lateinit var userId: String

    private lateinit var timer: CountDownTimer
//    private var duration: Long = 300000 // Durasi awal dalam milidetik (5 menit)
    private var duration: Long = 3000
    private lateinit var meja: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        userId = auth.currentUser!!.uid

        // Mendapatkan Intent yang memicu Activity ini
        val intent = intent

        // Mengecek apakah Intent memiliki data ekstra dengan kunci "Meja"
        if (intent.hasExtra("Meja")) {
            // Mendapatkan nilai data ekstra dengan kunci "Meja"
            meja = intent.getStringExtra("Meja").toString()
        }

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = String.format(
                    Locale.getDefault(),
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                )

                val hourMinSec = time.split(":")

                binding.TVHour.text = hourMinSec[0]
                binding.TVMinute.text = hourMinSec[1]
                binding.TVSecond.text = hourMinSec[2]
            }

            override fun onFinish() {
                val intent = Intent(this@CountDownTableActivity, HomeActivity::class.java)
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val sampai = (hour * 60) + minute + 1
                val newData = HashMap<String, Any>()
                newData["Banned"] = sampai.toInt()

                val documentReference: DocumentReference = store.collection("users").document(userId)
                documentReference.set(newData, SetOptions.merge())

                val newData2 = HashMap<String, Any>()
                newData2["$meja"] = "Kosong"

                val documentReference2: DocumentReference = store.collection("meja").document("reservasimeja")
                documentReference2.set(newData2, SetOptions.merge())
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }
}
