package com.example.YANK_application.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.YANK_application.R
import com.example.YANK_application.databinding.ActivityDeskripsiMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class DeskripsiMenuActivity : AppCompatActivity() {
    lateinit var binding:ActivityDeskripsiMenuBinding

    private var counter = 0
    private lateinit var myTextView: TextView
    private lateinit var harga: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var snapshotListener: ListenerRegistration
    lateinit var userId: String
    lateinit var meja: String
    var price: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeskripsiMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari Intent
        val judul = intent.getStringExtra("judul")
        val gambar = intent.getIntExtra("gambar", 0)
        price = intent.getIntExtra("price", 0)

        // Set judul TextView
        val tvJudul = binding.TVJudul
        tvJudul.text = judul
        binding.TVHarga.text = "Rp."+price.toString()

        var cardView = binding.CVIVMenuDesk
        cardView.radius = 40f

        var desk = judul?.replace(" ", "_")
        val resourceId = resources.getIdentifier(desk, "string", packageName)
        val deskripsiMenu = if (resourceId != 0) {
            getString(resourceId)
        } else {
            // String tidak ditemukan, lakukan tindakan yang sesuai
            "Error"
        }
        binding.TVDesk.text = deskripsiMenu


        // Set gambar ImageView
        val ivGambar = binding.IVMenuDesk
        ivGambar.setImageResource(gambar)

        myTextView = binding.jmlhBeli

        binding.addjmlh.setOnClickListener {
            counter++
            myTextView.text = counter.toString()
        }

        binding.lessjmlh.setOnClickListener {
            counter--
            if (counter <= 0){
                counter = 0
            }
            myTextView.text = counter.toString()
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

//    fun Tambah(view: View) {

//        if (meja == "") {
//            showToast("Booking table first")
//        }else{
//            var nama = binding.TVJudul
//            var total = binding.jmlhBeli.text.toString().toInt()
//            var harga = price * total
//
//            var semua = "$nama"+"$total"+"$harga"
//
//            val documentReference: DocumentReference = store.collection("users").document("$userId")
//            documentReference.addSnapshotListener(this@DeskripsiMenuActivity, EventListener<DocumentSnapshot> { documentSnapshot, e ->
//                if (e != null) {
//                    Log.d(ContentValues.TAG, "Error: $e")
//                    return@EventListener
//                }
//                val newData = HashMap<String, Any>()
//                val pesanan = documentSnapshot?.getString("Pesanan")
//
//                newData["Pesanan"] = "$pesanan"+"$semua"
//
//                val documentReference: DocumentReference = store.collection("users").document("$userId")
//                documentReference.set(newData, SetOptions.merge())
//                    .addOnSuccessListener {
//                        showToast("Ordered Successfully")
//                    }
//                    .addOnFailureListener { e ->
//                        showToast("Failed to Order: ${e.message}")
//                    }
//
//                })
//        }
//        snapshotListener.remove()
//    }


//    fun Tambah(view: View) {
//        if (meja == "") {
//            showToast("Booking table first")
//        } else {
//            var nama = binding.TVJudul.text.toString()
//            var total = binding.jmlhBeli.text.toString().toInt()
//            var harga = price * total
//
//            var semua = "$nama, $total, $harga"
//
//            val documentReference: DocumentReference = store.collection("users").document("$userId")
//            documentReference.addSnapshotListener(this@DeskripsiMenuActivity, EventListener<DocumentSnapshot> { documentSnapshot, e ->
//                if (e != null) {
//                    Log.d(ContentValues.TAG, "Error: $e")
//                    return@EventListener
//                }
//                val newData = HashMap<String, Any>()
//                val pesanan = documentSnapshot?.getString("Pesanan")
//
//                newData["Pesanan"] = "$pesanan $semua"
//
//                val documentReference: DocumentReference = store.collection("users").document("$userId")
//                documentReference.set(newData, SetOptions.merge())
//                    .addOnSuccessListener {
//                        runOnUiThread {
//                            showToast("Ordered Successfully")
//                        }
//                    }
//                    .addOnFailureListener { e ->
//                        runOnUiThread {
//                            showToast("Failed to Order: ${e.message}")
//                        }
//                    }
//            })
//        }
//    }


//    fun Tambah(view: View) {
//        auth = FirebaseAuth.getInstance()
//        userId = auth.currentUser!!.uid
//        store = FirebaseFirestore.getInstance()
//
//        val documentReference: DocumentReference = store.collection("users").document(userId)
//        snapshotListener = documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
//            if (documentSnapshot != null && documentSnapshot.exists()) {
//                meja = documentSnapshot.getString("Meja").toString()
//
//                if (meja == "") {
//                    showToast("Booking table first")
//                } else {
//                    val nama = binding.TVJudul.text.toString()
//                    val total = binding.jmlhBeli.text.toString().toInt()
//                    val harga = price * total
//
//                    val semua = "$nama,   $total,   $harga"
//
//                    documentReference.get().addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val documentSnapshot = task.result
//                            if (documentSnapshot != null && documentSnapshot.exists()) {
//                                val pesanan = documentSnapshot.getString("Pesan") ?: ""
//                                val newData = hashMapOf<String, Any>()
//                                newData["Pesan"] = "$pesanan $semua"
//
//                                documentReference.set(newData, SetOptions.merge())
//                                    .addOnSuccessListener {
//
//                                        runOnUiThread {
//                                            showToast("Ordered Successfully")
//                                        }
//                                    }
//                                    .addOnFailureListener { e ->
//                                        runOnUiThread {
//                                            showToast("Failed to Order: ${e.message}")
//                                            finishAffinity()
//                                        }
//                                    }
//                            } else {
//                                showToast("Document does not exist")
//                            }
//                        } else {
//                            showToast("Error: ${task.exception?.message}")
//                        }
//                    }
//                }
//            }
//        })
//    }


    var isDataInputted = false

    fun Tambah(view: View) {
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        store = FirebaseFirestore.getInstance()

        val documentReference: DocumentReference = store.collection("users").document(userId)
        snapshotListener = documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                meja = documentSnapshot.getString("Meja").toString()

                if (!isDataInputted && meja == "") {
                    showToast("Booking table first")
                } else if (!isDataInputted && meja != "") {
                    val nama = binding.TVJudul.text.toString()
                    val total = binding.jmlhBeli.text.toString().toInt()
                    val harga = price * total

                    val semua = "$nama,   $total,   $harga; \n "

                    documentReference.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val documentSnapshot = task.result
                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                val pesanan = documentSnapshot.getString("Pesan") ?: ""
                                val newData = hashMapOf<String, Any>()
                                newData["Pesan"] = "$pesanan $semua"

                                documentReference.set(newData, SetOptions.merge())
                                    .addOnSuccessListener {
                                        runOnUiThread {
                                            showToast("Ordered Successfully")
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        runOnUiThread {
                                            showToast("Failed to Order: ${e.message}")
                                        }
                                    }
                            } else {
                                showToast("Document does not exist")
                            }
                        } else {
                            showToast("Error: ${task.exception?.message}")
                        }
                    }

                    isDataInputted = true // Set isDataInputted menjadi true karena data sudah diinputkan
                }
            }
        })
    }
}
