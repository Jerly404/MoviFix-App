package com.hcondor.movifix.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.hcondor.movifix.R

class CongratulationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_congratulations, container, false)

        val goToHomeButton = view.findViewById<Button>(R.id.goToHomeButton)

        goToHomeButton.setOnClickListener {
            // Aquí puedes redirigir a la pantalla principal o actividad del Home
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }
}
