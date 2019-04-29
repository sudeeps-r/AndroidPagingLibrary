package com.movie.app.view.book_mark

import androidx.lifecycle.MutableLiveData
import com.movie.app.core.domain.model.BookMark
import com.movie.app.core.domain.model.Movie
import com.movie.app.core.util.DBState
import com.movie.app.core.util.DStatus
import com.movie.app.view.util.BaseViewModel
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class BookmarkViewModel @Inject constructor(val bookmarkRepo: BookmarkRepo):BaseViewModel() {

    private val mutableList:MutableLiveData<List<BookMark>> by lazy {
        MutableLiveData<List<BookMark>>()
    }


    fun getAllData(){
        bookmarkRepo.getAllBookMark().doOnSubscribe {
            compositeDp.addAll(it)
        }.subscribeWith(object : DisposableSingleObserver<List<BookMark>>(){
            override fun onSuccess(t: List<BookMark>) {
                mutableList.postValue(t)
            }

            override fun onError(e: Throwable) {
                setDBError("No bookmark found, start browsing the movies")
            }


        })
    }

    fun deleteBookMark(movie: Movie){
        bookmarkRepo.deleteMovie(movie).doOnSubscribe {
            compositeDp.addAll(it)
        }.subscribeWith(object :DisposableSingleObserver<Int>(){
            override fun onSuccess(t: Int) {
                movie.isBookmarked=false
                setState(DBState(DStatus.DELETE,"Successfully removed "+movie.title,movie))
            }

            override fun onError(e: Throwable) {
                setDBError("Failed to delete "+movie.title)
            }

        })
    }

    fun getAllBookMark()=mutableList
}