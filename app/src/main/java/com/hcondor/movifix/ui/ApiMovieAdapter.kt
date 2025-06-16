package com.hcondor.movifix.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcondor.movifix.R
import com.hcondor.movifix.model.Movie

class ApiMovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<ApiMovieAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val img: ImageView = v.findViewById(R.id.ivPoster)
        val title: TextView = v.findViewById(R.id.tvTitle)
        val year: TextView = v.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val m = movies[pos]
        holder.title.text = m.title
        holder.year.text = m.year
        Glide.with(holder.itemView.context)
            .load(m.imageUrl)
            .into(holder.img)
    }

    override fun getItemCount() = movies.size
}

