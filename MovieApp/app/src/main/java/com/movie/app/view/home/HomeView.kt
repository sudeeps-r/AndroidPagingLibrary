package com.movie.app.view.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movie.app.R
import com.movie.app.core.di.component.Injectable
import com.movie.app.core.util.NetworkState
import com.movie.app.databinding.ListViewBinding
import com.movie.app.view.util.*
import com.weatherapp.view.binding_adapter.autoCleared
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.movie.app.core.domain.model.Movie
import com.movie.app.core.util.DBState
import com.movie.app.core.util.DStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import com.movie.app.view.util.RxSearchObservable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.list_view.*


/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class HomeView : BaseFragment(), Injectable, RetryCallback, MovieItemListener {

    override fun onBookMark(movie: Movie) {
        if (movie.isBookmarked) {
            homeViewModel.deleteBookMark(movie)
        } else {
            homeViewModel.bookmark(movie)
        }
    }

    override fun onItemClicked(movie: Movie) {
        navbar().navigate(HomeViewDirections.action_homeView_to_details(movie.imdbID, movie.title))
    }

    override fun onRetry() {
        fetchMovies(searchQuery)
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeViewModel: HomeViewModel
    private var databing by autoCleared<ListViewBinding>()
    private lateinit var movieAdapter: MovieAdapter
    private var searchQuery: String = "friends"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.home_menu, menu)
        initSearchView(menu!!.findItem(R.id.m_search).actionView as SearchView)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBindingUtil = DataBindingUtil.inflate<ListViewBinding>(inflater, R.layout.list_view, container, false)
        databing = dataBindingUtil
        databing.setLifecycleOwner(this)
        return databing.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
    }

    private fun initSearchView(searchView: SearchView) {
        RxSearchObservable.fromView(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<String>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: String) {
                        if (t != null && t.length > 2)
                            searchQuery = t
                        fetchMovies(t!!)
                    }

                    override fun onError(e: Throwable) {

                    }

                })


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        retainInstance = true
    }

    private fun init() {
        hideAll()
        val listView = rvListView
        setLayoutManager(listView, LinearLayoutManager.VERTICAL)
        databing.retry = this
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        movieAdapter = MovieAdapter(homeViewModel, this)
        listView.adapter = movieAdapter
        val cache = homeViewModel.getCachedMovieList().value

        homeViewModel.getRefreshState().observe(this, Observer(::handleFirstLoad))
        homeViewModel.getNetworkState().observe(this, Observer(movieAdapter::setNetworkState))
        homeViewModel.getDBState().observe(this, Observer(::hanldeDBCallback))
        listView.addItemDecoration(SimpleDividerItemDecoration(context!!))
        if (cache != null) {
            homeViewModel.getCache().observe(this, Observer {movieAdapter.submitList(it)})
            homeViewModel.synchTheBookMarkStatus()
        } else {
            fetchMovies(searchQuery)

        }


    }

    val getListView by lazy {
        view?.findViewById(R.id.rvListView) as RecyclerView
    }

    fun fetchMovies(query: String) {
        if (query.length > 0) {
            homeViewModel.getMovieList(query).observe(this, Observer(movieAdapter::submitList))
        }

    }

    fun hanldeDBCallback(dbState: DBState) {
        when (dbState.status) {

            DStatus.INSERT, DStatus.DELETE -> {
                val m = dbState.obj as Movie
                movieAdapter.notifyItemChanged(m.pos, m)
            }

            else -> {
                showMessage(dbState.message!!)
            }
        }
    }

    fun handleFirstLoad(networkState: NetworkState) {

        when (networkState) {
            NetworkState.LOADED -> {
                hideAll()
            }
            NetworkState.LOADING -> {
                showLoader(false);showLoader(true);
                Timber.e("Loading")
            }
            else -> {
                showMessage("Error")
                showLoader(false)
                showError(true)
                databing.message = networkState.message

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.m_bookmark -> navbar().navigate(R.id.navigate_to_bookmark)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        getListView.invalidate()
    }
}