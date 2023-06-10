package com.example.YANK_application

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityTableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class TableActivity : AppCompatActivity() {

    lateinit var binding: ActivityTableBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()


        binding.button.setOnClickListener{
            updateDataMeja("A1")
        }
        binding.button2.setOnClickListener{
            updateDataMeja("A2")
        }
        binding.button3.setOnClickListener{
            updateDataMeja("A3")
        }
        binding.button4.setOnClickListener{
            updateDataMeja("A4")
        }
        binding.button5.setOnClickListener{
//            val A5 = binding.button5.text.toString().trim()
            updateDataMeja("A5")
        }





//                for (i in 0 until 6) {
//                    val meja = "A$i"
//                    if (meja == "Di pesan") {
//                        binding.button.isEnabled = false
//                        binding.button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
//                        binding.button.setTextColor(ContextCompat.getColor(this, R.color.white))
//                    } else{
//                        binding.button.isEnabled = true
//                        binding.button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
//                        binding.button.setTextColor(ContextCompat.getColor(this, R.color.black))
//                    }
//                }

    }

    fun updateDataMeja(meja: String) {
        val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Error: $e")
                return@EventListener
            }
        val newData = HashMap<String, Any>()
            val cek = documentSnapshot?.getString(meja)

            if (cek == "Kosong"){
                newData[meja] = "Di pesan"

                val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
                documentReference.set(newData, SetOptions.merge())
                    .addOnSuccessListener {
                        showToast("Berhasil Mengakses Meja")
                        val intent = Intent(this, CountDownTableActivity::class.java)
                        intent.putExtra("Meja", meja)
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        showToast("Gagal Memesan Meja: ${e.message}")
                    }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val buttonList = listOf(
            binding.button, binding.button2, binding.button3, binding.button4, binding.button5
        )
        val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Error: $e")
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val fieldMap = documentSnapshot.data
                fieldMap?.forEach { (key, value) ->
                    val index = key.substring(1).toIntOrNull()
                    if (index != null && index in 1..5) {
                        val button = buttonList[index - 1]
                        if (value == "Di pesan") {
                            button.isEnabled = false
                            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                            button.setTextColor(ContextCompat.getColor(this, R.color.white))
                        } else {
                            button.isEnabled = true
                            button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                            button.setTextColor(ContextCompat.getColor(this, R.color.black))
                        }
                    }
                }
            }
        })

    }
}