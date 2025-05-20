package com.hcondor.movifix.ui

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hcondor.movifix.R

class LoginChoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_choice)

        val googleButton = findViewById<Button>(R.id.googleButton)
        val facebookButton = findViewById<Button>(R.id.facebookButton)
        val signInButton = findViewById<Button>(R.id.signInButton)

        googleButton.setOnClickListener {
            // TODO: Lógica para continuar con Google
        }

        facebookButton.setOnClickListener {
            // TODO: Lógica para continuar con Facebook
        }

        signInButton.setOnClickListener {
            // TODO: Navegar al formulario de login
        }
    }
}
