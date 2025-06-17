package com.hcondor.movifix.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hcondor.movifix.R
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageFirma: ImageView
    private lateinit var btnFirmar: Button

    private val REQUEST_SIGNATURE = 2001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_video -> {
                    startActivity(Intent(this, VideosActivity::class.java))
                    true
                }
                R.id.nav_favorite -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }


        imageFirma = findViewById(R.id.image_firma)
        btnFirmar = findViewById(R.id.btn_firmar)

        btnFirmar.setOnClickListener {
            val intent = Intent(this, SignatureActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNATURE)
        }

        mostrarFirmaGuardada()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNATURE && resultCode == Activity.RESULT_OK) {
            mostrarFirmaGuardada()
        }
    }

    private fun mostrarFirmaGuardada() {
        val file = File(filesDir, "firma.png")
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imageFirma.setImageBitmap(bitmap)
        }
    }
}
