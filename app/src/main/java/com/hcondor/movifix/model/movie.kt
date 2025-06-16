package com.hcondor.movifix.model

import com.google.gson.annotations.SerializedName

data class Movie(
    // Para la API OMDb:
    @SerializedName("Title")
    val title: String = "",

    @SerializedName("Year")
    val year: String = "",

    @SerializedName("Poster")
    val imageUrl: String = "",

    // Campos locales (SQLite) o detalle:
    val description: String = "",
    val authors: String = "",
    val videoUrl: String = ""
)
