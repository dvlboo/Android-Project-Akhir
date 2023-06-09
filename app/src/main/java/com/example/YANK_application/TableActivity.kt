package com.example.YANK_application

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    lateinit var table: String

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

        // get data firestore sesuai id
        val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Error: $e")
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val A1 = documentSnapshot.getString("A1")
                val A2 = documentSnapshot.getString("A2")
                val A3 = documentSnapshot.getString("A3")
                val A4 = documentSnapshot.getString("A4")
                val A5 = documentSnapshot.getString("A5")

                if (A1 == "Di pesan") {
                    binding.button.isEnabled = false
                    binding.button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    binding.button.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else{
                    binding.button.isEnabled = true
                    binding.button.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                    binding.button.setTextColor(ContextCompat.getColor(this, R.color.black))
                }

                if (A2 == "Di pesan") {
                    binding.button2.isEnabled = false
                    binding.button2.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    binding.button2.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else{
                    binding.button2.isEnabled = true
                    binding.button2.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                    binding.button2.setTextColor(ContextCompat.getColor(this, R.color.black))
                }

                if (A3 == "Di pesan") {
                    binding.button3.isEnabled = false
                    binding.button3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    binding.button3.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else{
                    binding.button3.isEnabled = true
                    binding.button3.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                    binding.button3.setTextColor(ContextCompat.getColor(this, R.color.black))
                }

                if (A4 == "Di pesan") {
                    binding.button4.isEnabled = false
                    binding.button4.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    binding.button4.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else{
                    binding.button4.isEnabled = true
                    binding.button4.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                    binding.button4.setTextColor(ContextCompat.getColor(this, R.color.black))
                }

                if (A5 == "Di pesan") {
                    binding.button5.isEnabled = false
                    binding.button5.backgroundTintList = ContextCompat.getColorStateList(this, R.color.red)
                    binding.button5.setTextColor(ContextCompat.getColor(this, R.color.white))
                } else{
                    binding.button5.isEnabled = true
                    binding.button5.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
                    binding.button5.setTextColor(ContextCompat.getColor(this, R.color.black))
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
        })

    }

    fun updateDataMeja(meja: String) {
        val newData = HashMap<String, Any>()
        newData[meja] = "Di pesan"

        val documentReference: DocumentReference = store.collection("meja").document("reservasimeja")
        documentReference.set(newData, SetOptions.merge())
            .addOnSuccessListener {
                showToast("Berhasil Memesan Meja")
            }
            .addOnFailureListener { e ->
                showToast("Gagal Memesan Meja: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}