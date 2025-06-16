package com.hcondor.movifix.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hcondor.movifix.R
import com.hcondor.movifix.util.SignatureView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SignatureActivity : AppCompatActivity() {
    private lateinit var signatureView: SignatureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        signatureView = findViewById(R.id.signatureView)

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            signatureView.clear()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val bitmap = signatureView.saveToBitmap()
            val file = File(filesDir, "signature.png")

            try {
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                Toast.makeText(this, "Firma guardada en ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar la firma", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
