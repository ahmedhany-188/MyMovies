package com.example.mymovieapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mymovieapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var mainContent: ScrollView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        progressBar = view.findViewById(R.id.progressBar)
        errorLayout = view.findViewById(R.id.errorLayout)
        errorTextView = view.findViewById(R.id.errorTextView)
        retryButton = view.findViewById(R.id.retryButton)
        mainContent = view.findViewById(R.id.mainContent)

        // Initialize toolbar and set it as the support action bar.
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.detailToolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Override the toolbar's navigation click listener to trigger back navigation.
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val posterImage: ImageView = view.findViewById(R.id.detail_movie_poster)
        val titleText: TextView = view.findViewById(R.id.detail_movie_title)
        val overviewText: TextView = view.findViewById(R.id.detail_movie_overview)

        // Observe ViewModel state using Lifecycle.repeatOnLifecycle
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieState.collect { state ->
                    when (state) {
                        is DetailViewModel.MovieState.Loading -> showLoading()
                        is DetailViewModel.MovieState.Success -> {
                            showContent()
                            val movie = state.movie
                            titleText.text = movie.title
                            overviewText.text = movie.overview
                            Glide.with(requireContext())
                                .load(movie.posterUrl)
//                                .error(R.drawable.ic_error_outline)
                                .into(posterImage)
                        }
                        is DetailViewModel.MovieState.Error -> {
                            val errorMessage = when (state.errorType) {
                                DetailViewModel.ErrorType.NETWORK -> getString(R.string.error_no_internet)
                                DetailViewModel.ErrorType.SERVER -> getString(R.string.error_server)
                                DetailViewModel.ErrorType.GENERIC -> getString(R.string.error_generic)
                            }
                            showError(errorMessage)
                        }
                    }
                }
            }
        }

        // Load initial data
        viewModel.loadMovieDetails(args.movieId)

        // Retry button click listener
        retryButton.setOnClickListener {
            viewModel.loadMovieDetails(args.movieId)
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        mainContent.visibility = View.GONE
        errorLayout.visibility = View.GONE
    }

    private fun showContent() {
        progressBar.visibility = View.GONE
        mainContent.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        mainContent.visibility = View.GONE
        errorLayout.visibility = View.VISIBLE
        errorTextView.text = message
    }
}