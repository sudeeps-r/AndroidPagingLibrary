package com.movie.app.view.util

import androidx.recyclerview.widget.DiffUtil
import com.movie.app.core.domain.model.Movie


/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */

object MovieDiffUtil{
    var DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbID.equals(newItem.imdbID)
        }

        override fun areContentsTheSame( oldItem: Movie,  newItem: Movie): Boolean {
            return oldItem===newItem
        }
    }
}