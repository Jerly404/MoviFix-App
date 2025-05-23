package com.hcondor.movifix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.hcondor.movifix.R

class CustomizeFeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_customize_feed, container, false)

        val genreChipGroup = view.findViewById<ChipGroup>(R.id.genreChipGroup)
        val movieChipGroup = view.findViewById<ChipGroup>(R.id.movieChipGroup)
        val nextButton = view.findViewById<Button>(R.id.nextFeedButton)

        nextButton.setOnClickListener {
            val selectedGenres = genreChipGroup.checkedChipIds.size
            val selectedMovies = movieChipGroup.checkedChipIds.size

            if (selectedGenres < 3 || selectedMovies < 3) {
                Toast.makeText(requireContext(), "Select at least 3 genres and 3 movies", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.fragmentCustomizeFeed)
            }
        }

        return view
    }
}
