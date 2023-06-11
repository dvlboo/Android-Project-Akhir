package com.example.YANK_application

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.YANK_application.databinding.ActivityHomeBinding
import com.example.YANK_application.uitel.LoadingDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.ktx.Firebase
import java.util.*
import javax.annotation.Nullable
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    private var backPressed: Long = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    lateinit var userId: String
    var sampai: Int? = null
    var sekarang: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

        userId = auth.currentUser!!.uid

        val fullname = binding.TVFName
        val phone = binding.TVNoTelp
        val histori = binding.TVNoTelp

        // get data firestore sesuai id
//        val documentReference: DocumentReference = store.collection("users").document(userId)
//        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
//            if (e != null) {
//                Log.d(TAG, "Error: $e")
//                return@EventListener
//            }
//
//            if (documentSnapshot != null && documentSnapshot.exists()) {
//                fullname.text = documentSnapshot.getString("FName")
//                phone.text = documentSnapshot.getString("Phone")
//                histori.text = documentSnapshot.getString("Histori")
//            }
//        })

        // get data firestore sesuai id
        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (e != null) {
                Log.d(TAG, "Error: $e")
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                fullname.text = documentSnapshot.getString("FName")
                phone.text = documentSnapshot.getString("Phone")
                histori.text = documentSnapshot.getString("Histori")
            }
        })



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

    // untuk memperbarui data
    fun updateData(view: View) {
        val newData = HashMap<String, Any>()
        newData["FName"] = "Nama Baru"
        newData["UName"] = "Username Baru"
        newData["Email"] = "Email Baru"
        newData["Banned"] = 0

        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.set(newData, SetOptions.merge())
            .addOnSuccessListener {
                showToast("Data berhasil diperbarui")
            }
            .addOnFailureListener { e ->
                showToast("Gagal memperbarui data: ${e.message}")
            }
    }

    fun deleteAccount(view: View) {
        // mendapatkan id dan menghapus reference dari
        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.delete()
            .addOnSuccessListener {
                // Menghapus email dan password Login
                val user = Firebase.auth.currentUser
                user?.delete()?.addOnCompleteListener{
                    if(it.isSuccessful){
                        showToast("Akun berhasil di hapus")
                        startActivity(Intent(this, LoginActivity::class.java))
                    }else{
                        Log.e("error: ", it.exception.toString())
                    }
                }
            }
            .addOnFailureListener { e ->
                showToast("Gagal menghapus akun: ${e.message}")
            }
    }

    fun pesan_meja(view: View) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                sampai = documentSnapshot.getLong("Banned")?.toInt()
            }
                sekarang = (hour * 60) + minute

//                var xnow = this.sampai!! - sekarang!!

                if (sampai!! <= sekarang!! || sampai == 0) {
                    val newData = HashMap<String, Any>()
                    newData["Banned"] = 0

                    documentReference.set(newData, SetOptions.merge())

                    val intent = Intent(this, TableActivity::class.java)
                    startActivity(intent)

                } else {

                    fun formatAngka(angka: Int): String {
                        return String.format("%02d", angka)
                    }



                    val xsekarang = sampai!! - sekarang!!
                    val xmenit = sampai!! % 60
                    val xjam = sampai!! / 60

                    val xxmenit = formatAngka(xmenit)

                    showToast("Anda Telah diBanned selama $xsekarang menit. Tunggu Sampai $xjam:$xxmenit !!")
                }
        }.addOnFailureListener { e ->
            Log.d(TAG, "Error: $e")
        }
    }


}