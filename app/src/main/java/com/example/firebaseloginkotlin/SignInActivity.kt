package com.example.firebaseloginkotlin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebaseloginkotlin.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signInAppCompatButton.setOnClickListener {
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()

            when {
                mPassword.isEmpty() || mEmail.isEmpty() -> {
                    Toast.makeText(
                        this, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    signIn(mEmail, mPassword)
                }
            }

        }

        binding.signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            this.startActivity(intent)
        }

        binding.recoveryAccountTextView.setOnClickListener {
            val intent = Intent(this, AccountRecoveryActivity::class.java)
            this.startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                reload()
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Email o contraseña o incorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun reload() {
        //aqui agregar verificacion de suscripcion

        //verificarSuscripcion()
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
    }
}
/*
    private fun verificarSuscripcion() {
        //obtener el id de usuario
        val user = Firebase.auth.currentUser

        user?.let {
            val uid = user.uid
        }

        //instanciar la base de datos
        val suscripcion = Firebase.firestore.collection("suscripciones")

        //consultar suscripcion de usuario
        val query = suscripcion.whereEqualTo("id_usuario", "uid").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

}

    class Suscripciones {
    public var fecha_Inicio: Date
    public var fecha_Fin: Date
    public var id_Usuario: String

    public constructor(fecha_Inicio: Date, fecha_Fin: Date, id_Usuario: String)) {
        this.fecha_Inicio = fecha_Inicio
        this.fecha_Fin = fecha_Fin
        this.id_Usuario = id_Usuario
    }
}*/
