package com.example.YANK_application

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityForgetBinding
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ForgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetBinding
    var isTextViewVisible = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags


        binding.IVHideSee.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    hidesee()
                }
            })

        binding.IVHideSee2.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    hidesee()
                }
            })

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
            emailStream,
            passwordStream,
            password2Stream,
            otpStream
        ) { emailInvalid: Boolean, passwordInvalid: Boolean, password2Invalid: Boolean, otpInvalid: Boolean ->
            !emailInvalid && !passwordInvalid && !password2Invalid && !otpInvalid
        }
        invalidFieldStream.subscribe { isValid ->
            if (isValid){
                binding.BChange.isEnabled = true
                binding.BChange.backgroundTintList = ContextCompat.getColorStateList(this, R.color.teal_200)
            } else {
                binding.BChange.isEnabled = false
                binding.BChange.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }


    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.ETEmail.error = if (isNotValid)"Email tidak valid!" else null
    }

    private fun showOtpAlert(isNotValid: Boolean){
        binding.ETOtp.error = if (isNotValid)"OTP wajib di isi!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Password"){
            binding.ETPassword.error = if (isNotValid)"$text harus lebih dari 8 huruf!" else null
        }
    }
    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.ETPassword2.error = if (isNotValid)"Password tidak sama!" else null
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


}