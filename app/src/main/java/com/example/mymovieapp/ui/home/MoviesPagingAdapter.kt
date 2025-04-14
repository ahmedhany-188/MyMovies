package com.example.mymovieapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovieapp.domain.model.Movie
import com.example.mymovieapp.R

class MoviesPagingAdapter(private val onItemClick: (Movie) -> Unit, private val onFavoriteClick: (Movie) -> Unit )
    : PagingDataAdapter<Movie, MoviesPagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImage: ImageView = itemView.findViewById(R.id.movie_poster)
        private val titleText: TextView = itemView.findViewById(R.id.movie_title)
        private val releaseText: TextView = itemView.findViewById(R.id.movie_release_date)
        private val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)

        fun bind(movie: Movie?) {
            movie?.let {
                titleText.text = it.title
                releaseText.text = it.releaseDate
                Glide.with(itemView.context)
                    .load(it.posterUrl)
                    .into(posterImage)

                // Update favorite icon immediately
                favoriteIcon.setImageResource(
                    if (it.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )


                // Click the entire item to trigger navigation.
                itemView.setOnClickListener { onItemClick(movie) }

                favoriteIcon.setOnClickListener {
                    movie.isFavorite = !movie.isFavorite
                    favoriteIcon.setImageResource(
                        if (movie.isFavorite) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite_border
                    )
                    onFavoriteClick(movie)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem && oldItem.isFavorite == newItem.isFavorite
        }
    }
}
