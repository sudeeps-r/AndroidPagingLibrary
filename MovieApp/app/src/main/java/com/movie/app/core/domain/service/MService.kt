package com.movie.app.core.domain.service

import com.movie.app.core.domain.model.MovieDetails
import com.movie.app.core.domain.model.MovieResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Sudeep SR on 15/03/19.
 * Company <Reliance Payment Solutions Ltd.>
 * Email <sudeep.sr@ril.com>
 */
interface MService {

    @GET("/")
    fun searchMovies(@Query("s")query:String,@Query("page")page:Int): Observable<MovieResult>

    @GET("/")
    fun searchMovieDetails(@Query("i")imdbId:String): Observable<MovieDetails>

}