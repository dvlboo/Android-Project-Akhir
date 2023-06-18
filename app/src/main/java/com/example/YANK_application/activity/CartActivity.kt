package com.example.YANK_application.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.YANK_application.R
import com.example.YANK_application.adapter.FragmentPageAdapter
import com.example.YANK_application.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class CartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var meja: String

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

        userId = auth.currentUser!!.uid

        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val pesan = documentSnapshot.getString("Pesan")

                if (pesan != null) {
                    val items = pesan.split(";")
                    var total = 0

                    for (item in items) {
                        val values = item.trim().split(",")
                        val harga = values[values.lastIndex].trim().toIntOrNull()
                        if (harga != null) {
                            total += harga
                        }
//                        else {
//                            showToast("Terjadi kesalahan dalam mengambil harga")
//                            return@addOnSuccessListener
//                        }
                    }

                    // Menampilkan nilai total
//                    println("Total harga: $total")
                    binding.TVTotal.text = total.toString()
                } else {
                    showToast("Data pesan kosong")
                }
            } else {
                showToast("Dokumen tidak ditemukan")
            }
        }

        val document2Reference: DocumentReference = store.collection("users").document(userId)
        document2Reference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val pesan = documentSnapshot.getString("Pesan")

                val items = pesan?.split(";")

                val stringBuilder = StringBuilder()

                // Iterasi melalui setiap item
                for (item in items!!) {
                    // Memisahkan item berdasarkan tanda koma
                    val values = item.split(",")

                    // Pastikan jumlah elemen dalam values cukup
                    if (values.size >= 3) {
                        // Mengambil nilai nama, jumlah, dan harga
                        val nama = values[0].trim()
                        val jumlah = values[1].trim()
                        val harga = values[2].trim()

                        // Membuat string dengan format yang diinginkan
                        val itemString = "$nama   \t\t|  \t\t$jumlah    \t\t|    $harga\n"

                        // Menambahkan itemString ke stringBuilder
                        stringBuilder.append(itemString)
                    }
                }

                // Menampilkan hasilnya di TextView dengan ID TVDesk
                val textViewDesk = findViewById<TextView>(R.id.TVDesk)
                textViewDesk.text = stringBuilder.toString()
            }
        }


    }


    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    fun Selesai(view: View) {
        val documentReference3: DocumentReference = store.collection("users").document("$userId")
        documentReference3.get().addOnSuccessListener { documentSnapshot ->
            meja = documentSnapshot?.getString("Meja").toString()

            val newData2 = HashMap<String, Any>()
            newData2[meja] = "Kosong"
            val documentReference2: DocumentReference = store.collection("meja").document("reservasimeja")
            documentReference2.set(newData2, SetOptions.merge())
                .addOnSuccessListener {
                    val newData = HashMap<String, Any>()
                    newData["Meja"] = ""
                    val documentReference: DocumentReference = store.collection("users").document(userId)
                    documentReference.set(newData, SetOptions.merge())
                        .addOnSuccessListener {
                            val documentReference: DocumentReference = store.collection("users").document(userId)
                            documentReference.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val newData = HashMap<String, Any>()
                                    val banned = documentSnapshot.getLong("Banned")?.toInt()
                                    val fName = documentSnapshot.getString("FName")
                                    val histori = documentSnapshot.getString("Histori")
                                    val meja = documentSnapshot.getString("Meja")
                                    val phone = documentSnapshot.getString("Phone")
                                    val waktu = documentSnapshot.getString("Waktu")

                                    newData["FName"] = fName ?: ""
                                    newData["Banned"] = banned ?: "0"
                                    newData["Histori"] = histori ?: ""
                                    newData["Meja"] = meja ?: ""
                                    newData["Phone"] = phone ?: ""
                                    newData["Waktu"] = waktu ?: ""
                                    newData["Pesan"] = ""

                                    documentReference.set(newData, SetOptions.merge())
                                }
                            }
                            showToast("Terima kasih")
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            showToast("Gagal mengakhiri transaksi: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    showToast("Gagal mengubah status meja: ${e.message}")
                }
        }.addOnFailureListener { e ->
            showToast("Gagal mengambil data meja: ${e.message}")
        }
    }



    fun Pesan_Ulang(view: View) {
        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val newData = HashMap<String, Any>()
                val banned = documentSnapshot.getLong("Banned")?.toInt()
                val fName = documentSnapshot.getString("FName")
                val histori = documentSnapshot.getString("Histori")
                val meja = documentSnapshot.getString("Meja")
                val phone = documentSnapshot.getString("Phone")
                val waktu = documentSnapshot.getString("Waktu")

                newData["FName"] = fName ?: ""
                newData["Banned"] = banned ?: "0"
                newData["Histori"] = histori ?: ""
                newData["Meja"] = meja ?: ""
                newData["Phone"] = phone ?: ""
                newData["Waktu"] = waktu ?: ""
                newData["Pesan"] = ""

                documentReference.set(newData, SetOptions.merge())
                    .addOnSuccessListener {
                        showToast("Berhasil Mengakses Meja")
                        val intent = Intent(this, MenuActivity::class.java)
                        intent.putExtra("Meja", meja)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        showToast("Gagal Memesan Meja: ${e.message}")
                    }
            } else {
                showToast("Dokumen tidak ditemukan")
            }
        }.addOnFailureListener { e ->
            showToast("Error: ${e.message}")
        }
    }
}