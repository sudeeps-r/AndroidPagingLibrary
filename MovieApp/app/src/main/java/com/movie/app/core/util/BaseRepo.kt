package com.movie.app.core.util

import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
open class BaseRepo(private val bookmarkDao: BookmarkDao, private val schedulersFacade: SchedulersFacade) {


    fun deleteMovie(movie: Movie): Single<Int> {
        return bookmarkDao.deleteMovie(BookMark(movie.poster,movie.title,movie.type,movie.year,movie.imdbID)).subscribeOn(schedulersFacade.computation()).observeOn(schedulersFacade.ui())
    }

    fun insertMovie(movie: Movie): Completable {
        return bookmarkDao.bookmark(BookMark(movie.poster,movie.title,movie.type,movie.year,movie.imdbID)).subscribeOn(schedulersFacade.computation())
    }
}