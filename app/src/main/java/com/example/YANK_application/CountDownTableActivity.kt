package com.example.YANK_application

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.YANK_application.databinding.ActivityCountDownTableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.TimeUnit

class CountDownTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCountDownTableBinding

    private var backPressed: Long = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    lateinit var userId: String

    lateinit var codeScan: CodeScanner
    lateinit var hasilscan: String

    private lateinit var timer: CountDownTimer
    private var duration: Long = 300000 // Durasi awal dalam milidetik (5 menit)
//    private var duration: Long = 3000
    private lateinit var meja: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountDownTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        userId = auth.currentUser!!.uid

        codeScanner()
        setPermission()

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
                val intent = Intent(this@CountDownTableActivity, NewHomeActivity::class.java)
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val sampai = (hour * 60) + minute + 5
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

    private fun codeScanner() {
        codeScan = CodeScanner(this, binding.Scanner)

        codeScan.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            codeScan.isFlashEnabled = !codeScan.isFlashEnabled

            decodeCallback = DecodeCallback {
                runOnUiThread{
                    hasilscan = it.text

                    if (hasilscan == "1000$meja"){
                        showToast("berhasil masseee")
//                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        showToast("ngawor qr code e")
                    }
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    showToast("${it.message}")
                }
            }

            binding.Scanner.setOnClickListener {
                codeScan.startPreview()
            }

        }
    }

    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA), 101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            101 -> {
                if (grantResults. isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)
                        showToast("Permission Needed")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScan.startPreview()
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
        codeScan.startPreview()
    }

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            val newData = HashMap<String, Any>()
            newData[meja] = "Kosong"

            val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
            documentReference.set(newData, SetOptions.merge())
//            startActivity(Intent(this, TableActivity::class.java))
            finish()

        } else {
            showToast("Tekan Lagi untuk membatalkan pesan meja")
            backPressed = System.currentTimeMillis()
        }

    }
}
