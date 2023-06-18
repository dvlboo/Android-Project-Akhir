package com.example.YANK_application

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@Suppress("DEPRECATION")
@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {

    var login: SharedPreferences? = null
//    var akun: SharedPreferences? = null
    var no: SharedPreferences? = null

    var isTextViewVisible = true
    private lateinit var binding: ActivityLoginBinding
    private var backPressed: Long = 0

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(binding.root)

        // Auth
        auth = FirebaseAuth.getInstance()

//        no = getSharedPreferences("no", MODE_PRIVATE)

//        //memeriksa info login
//        // Mendapatkan instance dari SharedPreferences
//        login = getSharedPreferences("login", MODE_PRIVATE)
//        ceklog()

        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags


//        imageView.setOnClickListener(
//            object : View.OnClickListener {
//                override fun onClick(v: View) {
//                    if (isTextViewVisible) {
//                        binding.ETPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
//                        imageView.setImageResource(R.drawable.eye)
//                        isTextViewVisible = false
//                    } else {
//                        binding.ETPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//                        imageView.setImageResource(R.drawable.hide)
//                        isTextViewVisible = true
//                    }
//                }
//            })

        binding.BLogin.setOnClickListener{
            val email = binding.ETEmail.text.toString().trim()
            val password = binding.ETPassword.text.toString().trim()

//            // Menyimpan informasi login ke SharedPreferences
//            val editor = login!!.edit()
//            editor.putString("email", username)
//            editor.putString("password", password)
//            editor.apply()

            loginUser(email, password)


        }

        // Username validation
        val usernameStream = RxTextView.textChanges(binding.ETEmail)
            .skipInitialValue()
            .map { username ->
                username.isEmpty()
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Username")
        }

        // Password validation
        val passwordStream = RxTextView.textChanges(binding.ETPassword)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
//        passwordStream.subscribe {
//            showTextMinimalAlert(it,"Password")
//        }

        // button enable true or false
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            usernameStream,
            passwordStream,
            { usernameInvalid: Boolean, passwordInvalid: Boolean ->
                !usernameInvalid && !passwordInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid){
                binding.BLogin.isEnabled = true
                binding.BLogin.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            } else {
                binding.BLogin.isEnabled = false
                binding.BLogin.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_red_light)
            }
        }


    }

    private fun loginUser(email1: String, password1: String) {
        auth.signInWithEmailAndPassword(email1, password1)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    val verification = auth.currentUser?.isEmailVerified
                    if (verification == true){
                        val user = auth.currentUser
                        // Lanjutkan ke halaman berikutnya
                        Intent(this, NewHomeActivity::class.java).also{
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            showToast("Login Berhasil")
                        }
                    }else {
                        showToast("Mohon Verifikasi email anda! Cek di email")
                    }
                } else {
                    showToast(task.exception?.message.toString())
                }
            }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Email")
            binding.ETEmail.error = if (isNotValid)"$text Tidak Boleh Kosong!" else null
        else if (text == "Password")
            binding.ETPassword.error = if (isNotValid)"$text Tidak Boleh Kosong!" else null
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    // merubah tampilan ketika window focus
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideStatusBar()
        }
    }
    // menyembunyikan status bar
    private fun hideStatusBar() {
//        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
    }

    fun ClickMoveRegist(view: View) {
        intent = Intent(this, RegistActivity::class.java)
        startActivity(intent)
    }

    fun ClickMoveForget(view: View) {
        intent = Intent(this, ForgetActivity::class.java)
        startActivity(intent)
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

//    private fun ceklog() {
//        // Memeriksa apakah informasi login sudah tersimpan di SharedPreferences
//        if (login!!.contains("email") && login!!.contains("password")) {
//            // Jika informasi login sudah tersimpan, lanjutkan ke halaman berikutnya
//            val home = Intent(this, HomeActivity::class.java)
//            startActivity(home)
//            finish()
//        }
//        return
//    }



//    private fun dataakunbaru() {
//        akun = getSharedPreferences("akun", MODE_PRIVATE)
//
//        // Mendapatkan nilai array sebagai string dari SharedPreferences dengan menggunakan kunci atau key
//        val email = akun.getString("email", "")
//        val password = akun.getString("password", "")
//        val name = akun.getString("name", "")
//
//        // Memecah nilai array yang berupa string menjadi array biasa
//        emailrarr = email!!.replace("[", "").replace("]", "").replace(" ", "").split(",".toRegex())
//            .dropLastWhile { it.isEmpty() }
//            .toTypedArray()
//        pwarr = password!!.replace("[", "").replace("]", "").replace(" ", "").split(",".toRegex())
//            .dropLastWhile { it.isEmpty() }
//            .toTypedArray()
//        namearr = name!!.replace("[", "").replace("]", "").split(",".toRegex())
//            .dropLastWhile { it.isEmpty() }
//            .toTypedArray()
//        return
//    }




}