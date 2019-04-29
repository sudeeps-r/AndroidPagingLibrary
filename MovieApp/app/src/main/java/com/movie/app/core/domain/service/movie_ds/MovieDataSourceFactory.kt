package com.movie.app.core.domain.service.movie_ds

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.Movie
import com.movie.app.view.home.HomeRepo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class MovieDataSourceFactory @Inject constructor(val bookmarkDao: BookmarkDao,val homeRepo: HomeRepo,val schedulersFacade: SchedulersFacade) : DataSource.Factory<Integer, Movie>() {

    private val compositeDisposable = CompositeDisposable()
    private val movieDataSource: MovieDataSource

    init {
        movieDataSource = MovieDataSource(bookmarkDao,homeRepo, compositeDisposable,schedulersFacade)
    }

    private val movieResult: MutableLiveData<MovieDataSource> by lazy {
        MutableLiveData<MovieDataSource>()
    }

    fun getMovieResultObj() = movieResult

    fun getDisposable() = compositeDisposable


    override fun create(): DataSource<Integer, Movie> {
        movieResult.postValue(movieDataSource)
        return movieDataSource
    }

    fun setQuery(query: String) {
        movieDataSource.setQuery(query)

    }

}