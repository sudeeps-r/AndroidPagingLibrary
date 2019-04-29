package com.movie.app.view.home

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.movie.app.core.domain.model.Movie
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.movie.app.R
import com.movie.app.core.util.NetworkState
import com.movie.app.view.util.MovieDiffUtil
import com.weatherapp.view.binding_adapter.AdapterViewHolder
import timber.log.Timber


/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class MovieAdapter(private val homeViewModel: HomeViewModel,private val itemClick:MovieItemListener):PagedListAdapter<Movie, AdapterViewHolder> (MovieDiffUtil.DIFF_CALLBACK){

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater, viewType, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.movie_item -> {val m=getItem(position) as Movie;m.pos=position;holder.bind(m,itemClick)}
            R.layout.item_network_state -> holder.bind(networkState as NetworkState,homeViewModel)
        }
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItem(position: Int): Movie? {
        return super.getItem(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.movie_item
        }
    }
    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        Timber.e("error"+newNetworkState!!.message)
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

}

