package com.example.YANK_application.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityForgetBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ForgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetBinding
    var isTextViewVisible = true

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags

        //Auth
        auth = FirebaseAuth.getInstance()

        // email validasi
        val emailStream = RxTextView.textChanges(binding.ETEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
//                email.isEmpty()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        binding.BChange.setOnClickListener{
            val email = binding.ETEmail.text.toString().trim()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { reset ->
                    if(reset.isSuccessful){
                        Intent(this, LoginActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            showToast("Periksa Email Anda untuk mengatur ulang kata sandi!")
                        }
                    }else{
                        showToast(reset.exception?.message.toString())
                    }
                }

        }

    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        if(isNotValid){
            binding.ETEmail.error = "Email tidak valid!"
            binding.BChange.isEnabled = false
            binding.BChange.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
        }else{
            binding.ETEmail.error = null
            binding.BChange.isEnabled = true
            binding.BChange.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_red_light)
        }
    }

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

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}