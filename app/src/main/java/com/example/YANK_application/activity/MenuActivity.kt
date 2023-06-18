package com.example.YANK_application.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.YANK_application.adapter.FragmentPageAdapter
import com.example.YANK_application.databinding.ActivityMenuBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MenuActivity : AppCompatActivity() {

    private lateinit var adapter: FragmentPageAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var userId: String
    private lateinit var meja: String

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tablayout = binding.TLMenu
        val viewpager2 = binding.VP2Tampilan

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()

        userId = auth.currentUser!!.uid

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        tablayout.addTab(tablayout.newTab().setText("Package"))
        tablayout.addTab(tablayout.newTab().setText("Drink"))
        tablayout.addTab(tablayout.newTab().setText("Desert"))

        viewpager2.adapter = adapter

        tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewpager2.currentItem = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tablayout.selectTab(tablayout.getTabAt(position))
            }
        })
    }

    override fun onBackPressed() {
        showToast("Tidak dapat keluar, silakan lanjutkan transaksi hingga selesai")
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun PindahTransaksi(view: View) {
        startActivity(Intent(this, CartActivity::class.java))
    }
}

