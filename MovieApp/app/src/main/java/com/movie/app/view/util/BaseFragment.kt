package com.movie.app.view.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movie.app.R
import com.movie.app.core.util.NetworkState

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
open class BaseFragment : Fragment() {


    protected fun showError(show: Boolean) {
        if(show){
            errorView.visibility=View.VISIBLE
        }else{
            errorView.visibility=View.GONE
        }

    }
    protected fun showLoader(show: Boolean) {
        if(show){
            loadingView.visibility=View.VISIBLE
        }else{
            loadingView.visibility=View.GONE
        }

    }

    protected fun hideAll(){
        showError(false)
        showLoader(false)
    }

    val errorView by lazy {
        view?.findViewById(R.id.errorView) as View
    }

    val loadingView by lazy {
        view?.findViewById(R.id.loadingView) as View
    }

    protected fun navbar() = NavHostFragment.findNavController(this)
}