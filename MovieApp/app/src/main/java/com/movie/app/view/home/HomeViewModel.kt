package com.movie.app.view.home

import com.movie.app.view.util.BaseViewModel
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.movie.app.core.domain.model.Movie
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.movie.app.core.domain.model.MovieResult
import com.movie.app.core.domain.service.movie_ds.MovieDataSource
import com.movie.app.core.domain.service.movie_ds.MovieDataSourceFactory
import com.movie.app.core.util.DBState
import com.movie.app.core.util.DStatus
import com.movie.app.core.util.NetworkState
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableAllSingle
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
import java.util.function.Consumer


/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class HomeViewModel @Inject constructor(val homeRepo: HomeRepo, val movieDataSourceFactory: MovieDataSourceFactory) : BaseViewModel() {


    private lateinit var listLiveData: LiveData<PagedList<Movie>>

    private val cachedData: MutableLiveData<PagedList<Movie>> by lazy {
        MutableLiveData<PagedList<Movie>>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    init {
        initializePaging()
    }

    private fun initializePaging() {

        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10).build()

        listLiveData = LivePagedListBuilder(movieDataSourceFactory, pagedListConfig)
                .build()

        // progressLoadStatus = movieDataSourceFactory.getNetworkResponse()
        compositeDisposable = movieDataSourceFactory.getDisposable()

    }

    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.getMovieResultObj(), { it.networkState })

    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.getMovieResultObj(), { it.initialLoad })

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun retry() {
        movieDataSourceFactory.getMovieResultObj().value?.retry()
    }

    fun refresh() {
        /* movieDataSourceFactory.getMovieResultObj().value!!.invalidate()*/
    }

    fun getMovieList(query: String): LiveData<PagedList<Movie>> {
        movieDataSourceFactory.setQuery(query)
        initializePaging()
        return listLiveData
    }

    fun getCachedMovieList(): LiveData<PagedList<Movie>> {
        return listLiveData
    }

    fun synchTheBookMarkStatus() {
        val it = listLiveData.value


        if(it!=null){
            Observable.just(it).doOnSubscribe {
                compositeDisposable.addAll(it)
            }.subscribeOn(homeRepo.scheduler.io())
                    .map { it->queryDB(it) }
                    .observeOn(homeRepo.scheduler.ui())

                    .subscribeWith(object :DisposableObserver<PagedList<Movie>>(){
                        override fun onComplete() {

                        }

                        override fun onNext(t: PagedList<Movie>) {
                            Timber.e("cache size"+t.size)

                            cachedData.postValue(t)

                        }

                        override fun onError(e: Throwable) {

                        }

                    })
        }

    }

    private fun queryDB(movieResult: PagedList<Movie>): PagedList<Movie> {

        if (movieResult != null && movieResult.size > 0) {
            val itr = movieResult.listIterator()
            while (itr.hasNext()) {
                var it = itr.next()
                val res = homeRepo.bookmarkDao.getBookMark(it.imdbID)
                if (res != null) it.isBookmarked = true else it.isBookmarked=false

            }

        }

        return movieResult
    }

    fun deleteBookMark(movie: Movie) {
        homeRepo.deleteMovie(movie).doOnSubscribe {
            compositeDisposable.addAll(it)
        }.subscribeWith(object : DisposableSingleObserver<Int>() {
            override fun onSuccess(t: Int) {
                movie.isBookmarked = false
                setState(DBState(DStatus.DELETE, "Successfully removed " + movie.title, movie))
            }

            override fun onError(e: Throwable) {
                setDBError("Failed to delete " + movie.title)
            }

        })
    }

    fun bookmark(movie: Movie) {
        homeRepo.insertMovie(movie).doOnSubscribe {
            compositeDisposable.addAll(it)
        }.subscribeWith(object : DisposableCompletableObserver() {
            override fun onComplete() {
                movie.isBookmarked = true
                setState(DBState(DStatus.INSERT, movie.title + " bookmarked", movie))
            }

            override fun onError(e: Throwable) {
                setDBError(movie.title + " already bookmarked")
            }

        })
    }

    fun getCache()=cachedData
}