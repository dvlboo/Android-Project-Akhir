package com.example.YANK_application

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.YANK_application.databinding.ActivityRegistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.rxbinding2.widget.RxTextView


@Suppress("DEPRECATION")
@SuppressLint("CheckResult")
class RegistActivity : AppCompatActivity() {
    var isTextViewVisible = true
    private lateinit var binding: ActivityRegistBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore

    lateinit var userID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Auth
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()


        window.statusBarColor = Color.TRANSPARENT
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags

        binding.IVHideSee.setOnClickListener { hidesee() }

        binding.IVHideSee2.setOnClickListener { hidesee() }

        binding.BCreate.setOnClickListener {
//            val password1 = binding.ETPassword.text.toString()
//            val password2 = binding.ETPassword2.text.toString()
//            val name = binding.ETName.text.toString()
//            val username = binding.ETUsername.text.toString()
//            val email = binding.ETEmail.text.toString()

                val email = binding.ETEmail.text.toString().trim()
                val password = binding.ETPassword.text.toString().trim()
                val name = binding.ETName.text.toString().trim()
                val phone = binding.ETNoTelp.text.toString().trim()
                registerAuth(email, password, name, phone)


//                intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)


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

        // phone validasi
        val phoneStream = RxTextView.textChanges(binding.ETNoTelp)
            .skipInitialValue()
            .map { phone ->
                phone.isEmpty()
            }
        phoneStream.subscribe {
            showPhoneExistAlert(it)
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


        // button enable true or false
        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            nameStream,
            phoneStream,
            emailStream,
            passwordStream,
            password2Stream,
        ) { nameInvalid: Boolean, phoneInvalid: Boolean, emailInvalid: Boolean, passwordInvalid: Boolean, password2Invalid: Boolean ->
            !nameInvalid && !phoneInvalid && !emailInvalid && !passwordInvalid && !password2Invalid
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

    private fun registerAuth(email: String, password: String, name: String, phone: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){

                    auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            showToast("Periksa Email anda dan Verifikasi Akun anda!")

                            // menyimpan data ke firestore {
                            userID = auth.currentUser!!.uid
                            val documentReference: DocumentReference = fstore.collection("users").document(userID)
                            val user: MutableMap<String, Any> = HashMap()
                            user["FName"] = name
                            user["Phone"] = phone
                            user["Waktu"] = "0"
                            user["Histori"] = ""
                            documentReference.set(user)
                            intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
//                                .addOnSuccessListener {
//                                    Log.d(TAG, "onSuccess: profil pengguna berhasil dibuat untuk $userID")
//                                }
//                                .addOnFailureListener { e ->
//                                    Log.d(TAG, "onFailure: ${e.toString()}")
//                                }

                        }
                        ?.addOnFailureListener{
                            showToast(it.toString())
                        }
                } else {
                    showToast(task.exception?.message.toString())
                }
//                if(it.isSuccessful){
//                    showToast("Berhasil Membuat Akun")
//                    // menyimpan data ke firestore {
//                    userID = auth.currentUser!!.uid
//                    val documentReference: DocumentReference = fstore.collection("users").document(userID)
//                    val user: MutableMap<String, Any> = HashMap()
//                    user["FName"] = name
//                    user["UName"] = username
//                    user["Email"] = email
//                    documentReference.set(user)
//                        .addOnSuccessListener {
//                            Log.d(TAG, "onSuccess: profil pengguna berhasil dibuat untuk $userID")
//                        }
//                        .addOnFailureListener { e ->
//                            Log.d(TAG, "onFailure: ${e.toString()}")
//                        }
//                    // } ////////
//                    intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
////                    showToast(it.exception?.message)
//                }
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

    private fun showPhoneExistAlert(isNotValid: Boolean){
        binding.ETNoTelp.error = if (isNotValid)"No Telp tidak boleh kosong!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        if (text == "Password")
            binding.ETPassword.error = if (isNotValid)"$text harus lebih dari 8 huruf!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean){
        binding.ETEmail.error = if (isNotValid)"Email tidak valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean){
        binding.ETPassword2.error = if (isNotValid)"Password tidak sama!" else null
    }
}