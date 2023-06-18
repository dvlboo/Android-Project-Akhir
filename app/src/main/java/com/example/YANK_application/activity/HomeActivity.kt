package com.example.YANK_application.activity


import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.models.SlideModel
import com.example.YANK_application.R
import com.example.YANK_application.activity.LoginActivity
import com.example.YANK_application.activity.TableActivity
import com.example.YANK_application.adapter.FragmentPageAdapter
import com.example.YANK_application.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*
import kotlin.collections.HashMap


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private var backPressed: Long = 0

    private lateinit var auth: FirebaseAuth
    private lateinit var store: FirebaseFirestore
    private lateinit var userId: String

    var sampai: Int? = null
    var sekarang: Int? = null

    val ImageList = arrayListOf<SlideModel>()

    private lateinit var adapter: FragmentPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
// list untuk memanggil gambar kukuh
        ImageList.add(SlideModel(R.drawable.all1, "Normal Delights Package"))
        ImageList.add(SlideModel(R.drawable.menu2, "Deluxe Experience Package"))
        ImageList.add(SlideModel(R.drawable.minum1, "Mojito"))
        ImageList.add(SlideModel(R.drawable.minum6, "Soju"))
        ImageList.add(SlideModel(R.drawable.desert1, "Ice Cream"))
        ImageList.add(SlideModel(R.drawable.desert3, "Fruid Salad"))
        ImageList.add(SlideModel("https://cdn1-production-images-kly.akamaized.net/XgOu1A7aObRozZFZYvbryXWXqUw=/469x625/smart/filters:quality(75):strip_icc():format(webp)/kly-media-production/medias/1381085/original/044517300_1477042368-hl.jpg"))

        binding.ImageSlider.setImageList(ImageList)

        // toggle bar sidebar icon navigation
        drawerLayout = findViewById(R.id.drawer_layout2)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Inflate layout header di NavigationView
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val fullname = headerView.findViewById<TextView>(R.id.tv_fullName)
        val email = headerView.findViewById<TextView>(R.id.tv_email)

        // Dapatkan email dan nama lengkap pengguna
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        store = FirebaseFirestore.getInstance()
        val userId = user?.uid

        // Lakukan operasi sesuai kebutuhan Anda, misalnya mengambil data pengguna dari Firestore dan mengatur teks fullname dan email ke TextView
        // Contoh:
        if (userId != null) {
            store.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userFullname = document.getString("FName")
                        val userEmail = user.email
                        fullname.text = userFullname
                        email.text = userEmail
                    }
                }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
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
            false
        }


        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags



        //
        var tablayout = binding.TLMenu
        var viewpager2 = binding.VP2Tampilan

        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        tablayout.addTab(tablayout.newTab().setText("Package"))
        tablayout.addTab(tablayout.newTab().setText("Drink"))
        tablayout.addTab(tablayout.newTab().setText("Desert"))

        viewpager2.adapter = adapter

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewpager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })
        viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tablayout.selectTab(tablayout.getTabAt(position))
            }
        })
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (backPressed + 2000 > System.currentTimeMillis()) {
            finishAffinity() // Mengakhiri aktivitas dan semua aktivitas di dalamnya
            super.onBackPressed()
        } else {
            showToast("Tekan lagi untuk keluar")
            backPressed = System.currentTimeMillis()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun logout(view: View) {
        auth.signOut()
        Intent(this, LoginActivity::class.java).also{
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            showToast("Logout Berhasil")
        }
    }


    fun pesan_meja(view: View) {

        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
        val user = auth.currentUser
        val userId = user?.uid
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val documentReference: DocumentReference = store.collection("users").document(userId!!)
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