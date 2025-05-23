package com.hcondor.movifix.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hcondor.movifix.R

class ProfileSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_selection)

        // Ajuste de padding para barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ProfileSelection)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a vistas
        val profile1Image = findViewById<ImageView>(R.id.profile1Image)
        val addProfile = findViewById<LinearLayout>(R.id.addProfile)
        val doneButton = findViewById<Button>(R.id.doneButton)
        val skipButton = findViewById<Button>(R.id.skipButton)

        // Click en perfil 1
        profile1Image.setOnClickListener {
            Toast.makeText(this, getString(R.string.profile_1) + " selected", Toast.LENGTH_SHORT).show()
            // Aquí puedes abrir la actividad con el perfil seleccionado
        }

        // Click en añadir perfil
        addProfile.setOnClickListener {
            Toast.makeText(this, getString(R.string.add_profile), Toast.LENGTH_SHORT).show()
            // Aquí puedes abrir la actividad para crear un nuevo perfil
        }

        // Click en Done
        doneButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.done), Toast.LENGTH_SHORT).show()
            // Acción al confirmar selección
        }

        // Click en Skip
        skipButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.skip), Toast.LENGTH_SHORT).show()
            // Acción al saltar selección
        }
    }
}