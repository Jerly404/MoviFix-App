package com.hcondor.movifix.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hcondor.movifix.R

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val PREFS_NAME = "prefs"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER = "remember"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val rememberMeCheckBox = findViewById<CheckBox>(R.id.rememberMeCheckBox)
        val loginButton = findViewById<Button>(R.id.LoginButton)
        val googleButton = findViewById<LinearLayout>(R.id.googleButton)
        val facebookButton = findViewById<LinearLayout>(R.id.facebookButton)

        loadCredentials(emailInput, passwordInput, rememberMeCheckBox)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (rememberMeCheckBox.isChecked) {
                            saveCredentials(email, password)
                        } else {
                            clearCredentials()
                        }
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ProfileSelectionActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Configuración Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        facebookButton.setOnClickListener {
            Toast.makeText(this, "Facebook login aún no implementado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveCredentials(email: String, password: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .putBoolean(KEY_REMEMBER, true)
            .apply()
    }

    private fun clearCredentials() {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_EMAIL)
            .remove(KEY_PASSWORD)
            .putBoolean(KEY_REMEMBER, false)
            .apply()
    }

    private fun loadCredentials(emailInput: EditText, passwordInput: EditText, rememberMeCheckBox: CheckBox) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val remember = prefs.getBoolean(KEY_REMEMBER, false)
        if (remember) {
            val savedEmail = prefs.getString(KEY_EMAIL, "")
            val savedPassword = prefs.getString(KEY_PASSWORD, "")
            emailInput.setText(savedEmail)
            passwordInput.setText(savedPassword)
            rememberMeCheckBox.isChecked = true
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google sign in successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileSelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
