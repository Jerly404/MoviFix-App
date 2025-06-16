package com.hcondor.movifix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hcondor.movifix.R

class ChooseAvatarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_avatar, container, false)

        val nextButton = view.findViewById<Button>(R.id.nextAvatarButton)

        nextButton.setOnClickListener {
            findNavController().navigate(R.id.fragmentChoseAvatar)
        }



        return view
    }
}
