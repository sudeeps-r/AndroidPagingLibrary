package com.movie.app.core.domain.service.movie_ds

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.movie.app.core.di.module.SchedulersFacade
import com.movie.app.core.domain.dao.BookmarkDao
import com.movie.app.core.domain.model.Movie
import com.movie.app.core.domain.model.MovieResult
import io.reactivex.functions.Action
import com.movie.app.core.util.NetworkState
import com.movie.app.view.home.HomeRepo
import com.movie.app.view.util.PagingLoader
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class MovieDataSource(val bookmarkDao: BookmarkDao,val homeRepo: HomeRepo, val compositeDisposable: CompositeDisposable,val schedulersFacade: SchedulersFacade) : PageKeyedDataSource<Integer, Movie>() {


    private var index: Int = 1
    private lateinit var query:String
    private var retryCompletable: Completable? = null


    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(schedulersFacade.io())
                    .observeOn(schedulersFacade.ui())
                    .subscribe({ }, { throwable -> Timber.e(throwable.message) }))
        }
    }



    override fun loadInitial(params: LoadInitialParams<Integer>, callback: LoadInitialCallback<Integer, Movie>) {
        fetchData(true, callback)

    }

    fun setQuery(query:String){
        this.query=query
    }

    override fun loadAfter(params: LoadParams<Integer>, callback: LoadCallback<Integer, Movie>) {
        fetchData(false, null, callback)
    }

    override fun loadBefore(params: LoadParams<Integer>, callback: LoadCallback<Integer, Movie>) {

    }

    private fun fetchData(initial: Boolean, callback1: LoadInitialCallback<Integer, Movie>? = null, callback2: LoadCallback<Integer, Movie>? = null) {




        homeRepo.getMovies(query, index)
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe {
                    compositeDisposable.addAll(it)
                    networkState.postValue(NetworkState.LOADING)
                    if(initial){
                        initialLoad.postValue(NetworkState.LOADING)
                    }
                }.map{
                    it->queryDB(it)
                }.observeOn(schedulersFacade.ui())
                .subscribeWith(object : DisposableObserver<MovieResult>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MovieResult) {


                        if(t.response.equals("True")){
                            networkState.postValue(NetworkState.LOADED)
                            if(initial){
                                initialLoad.postValue(NetworkState.LOADED)
                            }
                            index++
                            if (initial) {

                                callback1?.onResult(t.result, null, Integer(index))
                            } else {
                                callback2?.onResult(t.result, Integer(index))
                            }
                        }else{
                            val error = NetworkState.error(t.error)
                            // publish the error
                            networkState.postValue(error)
                            if(initial){
                                initialLoad.postValue(error)
                            }
                        }


                    }

                    override fun onError(e: Throwable) {
                       setRetry(Action { fetchData(initial,callback1,callback2) })
                        val error = NetworkState.error(e.message)
                        // publish the error
                        networkState.postValue(error)
                        if(initial){
                            initialLoad.postValue(error)
                        }

                    }

                })


    }

    private fun queryDB(movieResult: MovieResult):MovieResult{

        if(movieResult.result!=null && movieResult.result.size>0){
            val itr= movieResult.result.listIterator()
            while (itr.hasNext()){
                var it=itr.next()
                val res=bookmarkDao.getBookMark(it.imdbID)
                if(res!=null) it.isBookmarked=true
            }

        }

        return movieResult
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
}