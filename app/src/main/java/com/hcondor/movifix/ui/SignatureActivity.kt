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

class SignatureActivity : AppCompatActivity() {

    private lateinit var signatureView: SignatureView
    private lateinit var btnClear: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signature)

        signatureView = findViewById(R.id.signatureView)
        btnClear = findViewById(R.id.btn_clear)
        btnSave = findViewById(R.id.btn_save)

        btnClear.setOnClickListener {
            signatureView.clear()
        }

        btnSave.setOnClickListener {
            saveSignatureToInternalStorage(signatureView.saveToBitmap())
        }
    }

    private fun saveSignatureToInternalStorage(bitmap: Bitmap) {
        try {
            val file = File(filesDir, "firma.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            Toast.makeText(this, "Firma guardada correctamente", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar firma", Toast.LENGTH_SHORT).show()
        }
    }
}
