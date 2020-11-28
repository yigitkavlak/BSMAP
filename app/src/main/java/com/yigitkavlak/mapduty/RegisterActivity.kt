package com.yigitkavlak.mapduty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var fauth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fauth = FirebaseAuth.getInstance()

        val currenUser = fauth.currentUser
        if (currenUser != null) {
            val intent = Intent(applicationContext, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun registerClicked(view: View) {
        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()

        if (TextUtils.isEmpty(email)) {
            userEmailText.setError("Email Kısmı Boş Bırakılamaz!")
            return
        }
        if (TextUtils.isEmpty(password)) {
            userPasswordText.setError("Password Kısmı Boş Bırakılamaz!")
            return
        }
        if (password.length < 6) {
            userPasswordText.setError("Password kısmı 6 karakterden az olamaz!")
            return
        }

        fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(applicationContext, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            if (exception != null) {
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun loginClicked(view: View) {

        val email = userEmailText.text.toString()
        val password = userPasswordText.text.toString()

        fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                //SignedIn
                Toast.makeText(
                    applicationContext,
                    "Hoşgeldiniz: ${fauth.currentUser!!.email.toString()}",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(applicationContext, MapsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                applicationContext,
                exception.localizedMessage.toString(),
                Toast.LENGTH_LONG
            ).show()

        }
    }
}