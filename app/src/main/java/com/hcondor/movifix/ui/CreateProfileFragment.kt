package com.hcondor.movifix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hcondor.movifix.R

class CreateProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_profile, container, false)

        val nextButton = view.findViewById<Button>(R.id.nextButton)

        nextButton.setOnClickListener {
            findNavController().navigate(R.id.fragmentCreateProfile)
        }
        return view
    }
}
