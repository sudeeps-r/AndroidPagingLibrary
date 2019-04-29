package com.movie.app.view.movie_details

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.movie.app.R
import com.movie.app.core.di.component.Injectable
import com.movie.app.core.util.NetworkState
import com.movie.app.databinding.ListViewBinding
import com.movie.app.databinding.MovieDetailsBinding
import com.movie.app.view.home.HomeViewModel
import com.movie.app.view.util.BaseActivity
import com.movie.app.view.util.BaseFragment
import com.movie.app.view.util.RetryCallback
import com.weatherapp.view.binding_adapter.autoCleared
import kotlinx.android.synthetic.main.app_bar.*
import javax.inject.Inject

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class Details():BaseFragment() ,Injectable, RetryCallback{

    private var mid:String=""

    override fun onRetry() {
        fetchDetails(mid)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var detailsViewModel: DetailsViewModel
    private var databing by autoCleared<MovieDetailsBinding>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.mdetails_menu, menu)

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.m_bookmark -> navbar().navigate(R.id.action_details_to_bookMarkView)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBindingUtil = DataBindingUtil.inflate<MovieDetailsBinding>(inflater, R.layout.movie_details, container, false)
        databing = dataBindingUtil
        databing.setLifecycleOwner(this)
        return databing.root
    }

    private fun init(){
        detailsViewModel = ViewModelProviders.of(this,viewModelFactory).get(DetailsViewModel::class.java)
        detailsViewModel.getNetWorkState().observe(this, Observer (::handleLoader))
        detailsViewModel.getMovieDetailsObj().observe(this, Observer (databing::setMovieDetails))
        hideAll()
        databing.retry=this
        arguments?.let {
            mid=DetailsArgs.fromBundle(it).movieId
            fetchDetails( mid )
            ( activity as BaseActivity).my_toolbar.title=DetailsArgs.fromBundle(it).title
        }

    }

    private fun fetchDetails(id:String){
        detailsViewModel.fetchMovieDetails(id)

    }



    private fun handleLoader(networkState: NetworkState){
        when(networkState){
            NetworkState.LOADING->{showLoader(true);showError(false)}
            NetworkState.LOADED->{hideAll()}
            else->{
                showError(true)
                showLoader(false)
                databing.message=networkState.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideAll()
    }

}