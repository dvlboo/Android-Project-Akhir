package com.example.YANK_application.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.YANK_application.R
import com.example.YANK_application.databinding.ActivityProfileBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    lateinit var userId: String

    var isTextViewVisible = true
    var isTextViewVisible2 = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    // Tindakan yang diambil ketika menu "Home" diklik
                    // Contoh: Navigasi ke halaman beranda
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
//                    return@setNavigationItemSelectedListener true
                    showToast("Logout Berhasil")
                }
                // Tambahkan tindakan untuk item menu lainnya di sini (jika ada)
            }
            // Tutup drawer setelah item menu diklik
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

        userId = auth.currentUser!!.uid
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun IVEditNama(view: View) {
            if (isTextViewVisible) {
                var text = binding.TVnama.getText().toString()
                binding.ETnama.setText(text)
                binding.ETnama.setVisibility(View.VISIBLE)
                binding.TVnama.setVisibility(View.GONE)
                binding.IVEditNama.setImageResource(R.drawable.ic_save)
                isTextViewVisible = false
            } else {
                var text = binding.ETnama.getText().toString()
                val newData = HashMap<String, Any>()
                newData["FName"] = text

                val documentReference: DocumentReference = store.collection("users").document(userId)
                documentReference.set(newData, SetOptions.merge())
                    .addOnSuccessListener {
                        showToast("Sucessfull to change name")

                    }
                    .addOnFailureListener { e ->
                        showToast("Failed Change Name : ${e.message}")
                    }
                binding.TVnama.setText(text)
                binding.ETnama.setVisibility(View.GONE)
                binding.TVnama.setVisibility(View.VISIBLE)
                binding.IVEditNama.setImageResource(R.drawable.ic_pen)
                isTextViewVisible = true

            }
    }

    fun IVEditNo(view: View) {
        if (isTextViewVisible2) {
            var text = binding.TVno.getText().toString()
            binding.ETno.setText(text)
            binding.ETno.setVisibility(View.VISIBLE)
            binding.TVno.setVisibility(View.GONE)
            binding.IVEditNo.setImageResource(R.drawable.ic_save)
            isTextViewVisible2 = false
        } else {
            var text = binding.ETno.getText().toString()
            val newData = HashMap<String, Any>()
            newData["Phone"] = text

            val documentReference: DocumentReference = store.collection("users").document(userId)
            documentReference.set(newData, SetOptions.merge())
                .addOnSuccessListener {
                    showToast("Sucessfull to change Phone")

                }
                .addOnFailureListener { e ->
                    showToast("Failed Change Phone : ${e.message}")
                }
            binding.TVno.setText(text)
            binding.ETno.setVisibility(View.GONE)
            binding.TVno.setVisibility(View.VISIBLE)
            binding.IVEditNo.setImageResource(R.drawable.ic_pen)
            isTextViewVisible2 = true

        }
    }


    override fun onStart() {
        var Fname = binding.TVnama
        var No = binding.TVno
        super.onStart()
        // get data firestore sesuai id
        val documentReference: DocumentReference = store.collection("users").document(userId)
        documentReference.addSnapshotListener(this, EventListener<DocumentSnapshot> { documentSnapshot, e ->
            if (e != null) {
                Log.d(ContentValues.TAG, "Error: $e")
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                Fname.text = documentSnapshot.getString("FName")
                No.text = documentSnapshot.getString("Phone")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun Delete(view: View) {
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


}