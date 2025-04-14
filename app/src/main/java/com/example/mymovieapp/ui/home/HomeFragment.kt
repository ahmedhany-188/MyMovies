package com.example.mymovieapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Switch
import androidx.paging.LoadState
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView
import retrofit2.HttpException


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var recyclerMovies: RecyclerView
    private lateinit var viewToggle: Switch
    private lateinit var adapter: MoviesPagingAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: View
    private lateinit var errorTextView: TextView
    private lateinit var retryButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "onCreateView called.f..")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.homeToolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val exitButton = view.findViewById<ImageButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            // Exit the app
            requireActivity().finish()
        }

        Log.d("HomeFragment", "onViewCreated calledddd")
        recyclerMovies = view.findViewById(R.id.recycler_movies)
        viewToggle = view.findViewById(R.id.switch_view_mode)
        progressBar = view.findViewById(R.id.progressBar)
        errorLayout = view.findViewById(R.id.errorLayout)
        errorTextView = view.findViewById(R.id.errorTextView)
        retryButton = view.findViewById(R.id.retryButton)
        adapter = MoviesPagingAdapter(
            onItemClick = { movie ->
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(movie.id)
                findNavController().navigate(action)
            },
            onFavoriteClick = { movie ->
                lifecycleScope.launch {
                    viewModel.toggleFavorite(movie)
                }
            }
        )
        recyclerMovies.adapter = adapter

        recyclerMovies.layoutManager = LinearLayoutManager(requireContext())

        // Toggle between grid and list layouts
        viewToggle.setOnCheckedChangeListener { _, isChecked ->
            recyclerMovies.layoutManager = if (isChecked)
                GridLayoutManager(requireContext(), 2)
            else
                LinearLayoutManager(requireContext())
        }

        // Observe and submit paginated data
        lifecycleScope.launch {
            viewModel.movies.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
        // Listen to Paging 3 LoadState changes.
        // Modified LoadState listener
        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0

            // Show/hide main content
            view.findViewById<LinearLayout>(R.id.mainContent).visibility =
                if (isListEmpty || loadState.refresh is LoadState.Error) View.GONE else View.VISIBLE

            // Handle loading state
            progressBar.visibility = when (loadState.refresh) {
                is LoadState.Loading -> View.VISIBLE
                else -> View.GONE
            }

            // Handle error states
            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error

            errorState?.let { state ->
                errorLayout.visibility = View.VISIBLE
                errorTextView.text = when {
                    // Check for network connectivity
                    !isNetworkAvailable() -> getString(R.string.error_no_internet)
                    // Check for specific error codes
                    state.error is HttpException -> getString(R.string.error_server)
                    else -> getString(R.string.error_generic, state.error.localizedMessage)
                }
            } ?: run {
                errorLayout.visibility = View.GONE
            }

            // Handle empty state
            if (isListEmpty) {
                showEmptyState()
            } else {
                hideEmptyState()
            }
        }
        // Retry button action: When clicked, try reloading the data.
        retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    // Show empty state
    fun showEmptyState() {
        errorLayout.visibility = View.VISIBLE
        errorTextView.text = getString(R.string.empty_state_message)
        retryButton.visibility = View.GONE
    }

    // Show error state
//    fun showErrorState(message: String) {
//        errorLayout.visibility = View.VISIBLE
//        errorTextView.text = message
//        retryButton.visibility = View.VISIBLE
//    }

    private fun hideEmptyState() {
        errorLayout.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}

