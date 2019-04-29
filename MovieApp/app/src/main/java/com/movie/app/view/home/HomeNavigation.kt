package com.movie.app.view.home

import com.movie.app.core.domain.model.Movie

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
interface MovieItemListener {
    fun onItemClicked(movie:Movie)

    fun onBookMark(movie: Movie)
}