package com.movie.app.view.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.movie.app.core.domain.model.MovieDetails
import com.movie.app.core.util.NetworkState
import com.movie.app.view.util.BaseViewModel
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

/**
 * Created by Sudeep SR on 16/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
class DetailsViewModel @Inject constructor(val detailsRepo: DetailsRepo) : BaseViewModel() {

    private val movieDetails: MutableLiveData<MovieDetails> by lazy {
        MutableLiveData<MovieDetails>()
    }


    fun fetchMovieDetails(id: String) {
        detailsRepo.getMovieDetails(id).doOnSubscribe {
            compositeDp.addAll(it)
            setState(NetworkState.LOADING)
        }
                .subscribeWith(object : DisposableObserver<MovieDetails>() {
                    override fun onComplete() {

                    }

                    override fun onNext(t: MovieDetails) {
                        if (t.Response.equals("True")) {
                            setState(NetworkState.LOADED)
                            movieDetails.postValue(t)
                        } else {
                            setError("Invalid Id")
                        }
                    }

                    override fun onError(e: Throwable) {
                        setError(e!!.message!!)
                    }


                })
    }

    fun getMovieDetailsObj()=movieDetails
}