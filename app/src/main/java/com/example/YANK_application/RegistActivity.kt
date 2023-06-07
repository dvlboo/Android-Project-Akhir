package com.example.YANK_application

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityRegistBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView


@Suppress("DEPRECATION")
@SuppressLint("CheckResult")
class RegistActivity : AppCompatActivity() {
    var isTextViewVisible = true
    private lateinit var binding: ActivityRegistBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Auth
        auth = FirebaseAuth.getInstance()


        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags

        binding.IVHideSee.setOnClickListener { hidesee() }

        binding.IVHideSee2.setOnClickListener { hidesee() }

        binding.BCreate.setOnClickListener {
//            val password1 = binding.ETPassword.text.toString()
//            val password2 = binding.ETPassword2.text.toString()
            val otp = binding.ETOtp.text.toString()
//            val name = binding.ETName.text.toString()
//            val username = binding.ETUsername.text.toString()
//            val email = binding.ETEmail.text.toString()


            if (otp != "123456"){
                    showToast("OTP Salah")
            } else {
                val username = binding.ETUsername.text.toString().trim()
                val email = binding.ETEmail.text.toString().trim()
                val password = binding.ETPassword.text.toString().trim()
                registerAuth(email, password)

//                intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
            }

        }

        // fullname validasi
        val nameStream = RxTextView.textChanges(binding.ETName)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        nameStream.subscribe {
            showNameExistAlert(it)
        }

        // email validasi
        val emailStream = RxTextView.textChanges(binding.ETEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                //email.isEmpty()
            }
        emailStream.subscribe {
            showEmailValidAlert(it)
        }

        // Username validation
        val usernameStream = RxTextView.textChanges(binding.ETUsername)
            .skipInitialValue()
            .map { username ->
                username.length < 5
            }
        usernameStream.subscribe {
            showTextMinimalAlert(it,"Username")
        }

        // Password validation
        val passwordStream = RxTextView.textChanges(binding.ETPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it,"Password")
        }

        // Confirm Password validation
        val password2Stream = io.reactivex.Observable.merge(
//        val password2Stream = Observable.merge(
            RxTextView.textChanges(binding.ETPassword)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.ETPassword2.text.toString()
                },
            RxTextView.textChanges(binding.ETPassword2)
                .skipInitialValue()
                .map { confirmpassword ->
                    confirmpassword.toString() != binding.ETPassword.text.toString()
                })
        password2Stream.subscribe {
            showPasswordConfirmAlert(it)
        }

        // otp validasi
        val otpStream = RxTextView.textChanges(binding.ETOtp)
            .skipInitialValue()
            .map { otp ->
                otp.isEmpty()
            }
        otpStream.subscribe {
            showOtpAlert(it)
        }

        // button enable true or false
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            nameStream,
            emailStream,
            usernameStream,
            passwordStream,
            password2Stream,
            otpStream
        ) { nameInvalid: Boolean, emailInvalid: Boolean, usernameInvalid: Boolean, passwordInvalid: Boolean, password2Invalid: Boolean, otpInvalid: Boolean ->
            !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !password2Invalid && !otpInvalid
        }
        invalidFieldStream.subscribe { isValid ->
            if (isValid){
                binding.BCreate.isEnabled = true
                binding.BCreate.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
            } else {
                binding.BCreate.isEnabled = false
                binding.BCreate.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }
    }

    private fun registerAuth(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    showToast("Berhasil Membuat Akun")
                    intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
//                    showToast(it.exception?.message)
                }
            }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun hidesee() {
        if (isTextViewVisible) {
            binding.ETPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            binding.ETPassword2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            binding.IVHideSee.setImageResource(R.drawable.eye)
            binding.IVHideSee2.setImageResource(R.drawable.eye)
            isTextViewVisible = false
        } else {
            binding.ETPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ETPassword2.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.IVHideSee.setImageResource(R.drawable.hide)
            binding.IVHideSee2.setImageResource(R.drawable.hide)
            isTextViewVisible = true
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
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    private fun showNameExistAlert(isNotValid: Boolean){
        binding.ETName.error = if (isNotValid)"Nama tidak boleh kosong!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Username")
            binding.ETUsername.error = if (isNotValid)"$text harus lebih dari 5 huruf!" else null
        else if (text == "Password")
            binding.ETPassword.error = if (isNotValid)"$text harus lebih dari 8 huruf!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.ETEmail.error = if (isNotValid)"Email tidak valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.ETPassword2.error = if (isNotValid)"Password tidak sama!" else null
    }

    private fun showOtpAlert(isNotValid: Boolean){
        binding.ETOtp.error = if (isNotValid)"OTP wajib di isi!" else null
    }
}